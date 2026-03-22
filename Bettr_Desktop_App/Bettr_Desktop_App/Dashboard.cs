using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;
using Bettr_Desktop_App.Api;
using Bettr_Desktop_App.Models;

namespace Bettr_Desktop_App
{
    public partial class Dashboard : Form
    {
        private readonly ApiService _apiService = new ApiService();
        private string _currentPage = "feed";
        private string _habitImageBase64 = "";

        [System.Runtime.InteropServices.DllImport("Gdi32.dll", EntryPoint = "CreateRoundRectRgn")]
        private static extern IntPtr CreateRoundRectRgn
        (
            int nLeftRect,
            int nTopRect,
            int nRightRect,
            int nBottomRect,
            int nWidthEllipse,
            int nHeightEllipse
        );

        public Dashboard()
        {
            InitializeComponent();
            this.FormBorderStyle = FormBorderStyle.None;
            LoadIcon();
        }

        private void LoadIcon()
        {
            try
            {
                string iconPath = System.IO.Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "Resources", "app.ico");
                if (System.IO.File.Exists(iconPath))
                {
                    this.Icon = new Icon(iconPath);
                    picSidebarLogo.Image = Image.FromFile(iconPath);
                }
            }
            catch { }
        }

        private async void Dashboard_Load(object sender, EventArgs e)
        {
            panelContent.AutoScroll = true;

            if (ApiService.CurrentUser != null)
            {
                lblUserName.Text = ApiService.CurrentUser.Name ?? "Usuario";
                lblUserHandle.Text = "@" + (ApiService.CurrentUser.Username ?? "user");

                if (!string.IsNullOrEmpty(ApiService.CurrentUser.Avatar) && ApiService.CurrentUser.Avatar.StartsWith("data:image"))
                {
                    try
                    {
                        picUserAvatar.Image = Base64ToImage(ApiService.CurrentUser.Avatar);
                    }
                    catch { }
                }
                else
                {
                    lblAvatarInitial.Text = ApiService.CurrentUser.Name?[0].ToString().ToUpper() ?? "U";
                }
            }

            await LoadFeed();
        }

        private Image Base64ToImage(string base64)
        {
            base64 = base64.Replace("data:image/jpeg;base64,", "").Replace("data:image/png;base64,", "").Replace("data:image/gif;base64,", "");
            byte[] imageBytes = Convert.FromBase64String(base64);
            using (MemoryStream ms = new MemoryStream(imageBytes))
            {
                return Image.FromStream(ms);
            }
        }

        private async Task LoadFeed()
        {
            _currentPage = "feed";
            lblPageTitle.Text = "Inicio";
            ClearButtonsHighlight();
            btnNavHome.BackColor = Color.FromArgb(250, 204, 21);
            btnNavHome.ForeColor = Color.FromArgb(28, 31, 34);

            panelContent.Controls.Clear();

            Label loadingLabel = new Label
            {
                Text = "Cargando...",
                ForeColor = Color.FromArgb(209, 213, 219),
                AutoSize = true,
                Location = new Point(30, 30),
                Font = new Font("Segoe UI", 10)
            };
            panelContent.Controls.Add(loadingLabel);

            try
            {
                List<Habit> habits = await _apiService.GetFeedAsync(ApiService.CurrentUser.Id);
                panelContent.Controls.Clear();

                if (habits.Count == 0)
                {
                    AddEmptyState("No hay publicaciones", "Sigue a otros usuarios para ver sus hábitos");
                }
                else
                {
                    int yPos = 20;
                    foreach (var habit in habits)
                    {
                        AddHabitCard(habit, ref yPos);
                    }
                }
            }
            catch (Exception ex)
            {
                panelContent.Controls.Clear();
                AddEmptyState("Error", $"Error al cargar el feed: {ex.Message}");
            }
        }

        private async Task LoadMyHabits()
        {
            _currentPage = "habits";
            lblPageTitle.Text = "Mis Hábitos";
            ClearButtonsHighlight();
            btnNavHabits.BackColor = Color.FromArgb(250, 204, 21);
            btnNavHabits.ForeColor = Color.FromArgb(28, 31, 34);

            panelContent.Controls.Clear();

            Label loadingLabel = new Label
            {
                Text = "Cargando...",
                ForeColor = Color.FromArgb(209, 213, 219),
                AutoSize = true,
                Location = new Point(30, 30),
                Font = new Font("Segoe UI", 10)
            };
            panelContent.Controls.Add(loadingLabel);

            try
            {
                List<Habit> habits = await _apiService.GetUserHabitsAsync(ApiService.CurrentUser.Id);
                panelContent.Controls.Clear();

                if (habits.Count == 0)
                {
                    AddEmptyState("No tienes hábitos", "Comparte tu primer hábito con la comunidad");
                }
                else
                {
                    int yPos = 20;
                    foreach (var habit in habits)
                    {
                        AddHabitCard(habit, ref yPos);
                    }
                }
            }
            catch (Exception ex)
            {
                panelContent.Controls.Clear();
                AddEmptyState("Error", $"Error al cargar hábitos: {ex.Message}");
            }
        }

        private async Task LoadDiscover()
        {
            _currentPage = "discover";
            lblPageTitle.Text = "Descubrir";
            ClearButtonsHighlight();
            btnNavDiscover.BackColor = Color.FromArgb(250, 204, 21);
            btnNavDiscover.ForeColor = Color.FromArgb(28, 31, 34);

            panelContent.Controls.Clear();

            TextBox searchBox = new TextBox
            {
                BackColor = Color.FromArgb(55, 59, 65),
                ForeColor = Color.White,
                Location = new Point(30, 20),
                Size = new Size(panelContent.Width - 60, 35),
                Font = new Font("Segoe UI", 11),
                BorderStyle = BorderStyle.None,
                PlaceholderText = "Buscar usuarios..."
            };
            searchBox.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, searchBox.Width, searchBox.Height, 15, 15));
            searchBox.TextChanged += async (s, e) => await SearchUsers(searchBox.Text);
            panelContent.Controls.Add(searchBox);

            await SearchUsers("");
        }

        private async Task SearchUsers(string query)
        {
            int searchBoxHeight = 50;

            var controlsToRemove = panelContent.Controls.OfType<Panel>().Where(p => p.Name == "userCard").ToList();
            foreach (var c in controlsToRemove)
            {
                panelContent.Controls.Remove(c);
            }

            try
            {
                List<User> users = await _apiService.SearchUsersAsync(query);
                users = users.Where(u => u.Id != ApiService.CurrentUser.Id).ToList();

                int yPos = searchBoxHeight + 20;
                foreach (var user in users)
                {
                    bool isFollowing = await _apiService.IsFollowingAsync(ApiService.CurrentUser.Id, user.Id);
                    AddUserCard(user, isFollowing, ref yPos);
                }
            }
            catch { }
        }

        private async Task LoadProfile()
        {
            _currentPage = "profile";
            lblPageTitle.Text = "Mi Perfil";
            ClearButtonsHighlight();
            btnNavProfile.BackColor = Color.FromArgb(250, 204, 21);
            btnNavProfile.ForeColor = Color.FromArgb(28, 31, 34);

            panelContent.Controls.Clear();

            try
            {
                User user = await _apiService.GetUserByIdAsync(ApiService.CurrentUser.Id);
                if (user != null)
                {
                    ApiService.CurrentUser = user;
                }

                Dictionary<string, int> stats = await _apiService.GetUserStatsAsync(ApiService.CurrentUser.Id);

                Panel profileCard = new Panel
                {
                    BackColor = Color.FromArgb(42, 46, 51),
                    Location = new Point(30, 20),
                    Size = new Size(panelContent.Width - 60, 280),
                    Name = "profileCard"
                };
                profileCard.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, profileCard.Width, profileCard.Height, 20, 20));

                PictureBox avatar = new PictureBox
                {
                    Size = new Size(100, 100),
                    Location = new Point((profileCard.Width - 100) / 2, 30),
                    BackColor = Color.FromArgb(102, 126, 234),
                    SizeMode = PictureBoxSizeMode.Zoom
                };
                avatar.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, 100, 100, 50, 50));

                if (!string.IsNullOrEmpty(ApiService.CurrentUser.Avatar) && ApiService.CurrentUser.Avatar.StartsWith("data:image"))
                {
                    avatar.Image = Base64ToImage(ApiService.CurrentUser.Avatar);
                }
                else
                {
                    Label avatarLetter = new Label
                    {
                        Text = ApiService.CurrentUser.Name?[0].ToString().ToUpper() ?? "U",
                        Font = new Font("Segoe UI", 36, FontStyle.Bold),
                        ForeColor = Color.White,
                        AutoSize = false,
                        Size = avatar.Size,
                        TextAlign = ContentAlignment.MiddleCenter
                    };
                    avatar.Controls.Add(avatarLetter);
                }

                Label nameLabel = new Label
                {
                    Text = ApiService.CurrentUser.Name ?? "Usuario",
                    Font = new Font("Segoe UI", 16, FontStyle.Bold),
                    ForeColor = Color.White,
                    AutoSize = true,
                    Location = new Point((profileCard.Width - 200) / 2, 145)
                };

                Label usernameLabel = new Label
                {
                    Text = "@" + (ApiService.CurrentUser.Username ?? "user"),
                    Font = new Font("Segoe UI", 11),
                    ForeColor = Color.FromArgb(156, 163, 175),
                    AutoSize = true,
                    Location = new Point((profileCard.Width - 100) / 2, 170)
                };

                int statsY = 210;
                int statWidth = (profileCard.Width - 60) / 3;

                AddStatItem(profileCard, stats["followers"].ToString(), "Seguidores", 30, statsY, statWidth);
                AddStatItem(profileCard, stats["following"].ToString(), "Siguiendo", 30 + statWidth, statsY, statWidth);
                AddStatItem(profileCard, stats["habits"].ToString(), "Hábitos", 30 + statWidth * 2, statsY, statWidth);

                profileCard.Controls.Add(avatar);
                profileCard.Controls.Add(nameLabel);
                profileCard.Controls.Add(usernameLabel);

                Button logoutBtn = new Button
                {
                    Text = "Cerrar Sesión",
                    BackColor = Color.FromArgb(239, 68, 68),
                    ForeColor = Color.White,
                    FlatStyle = FlatStyle.Flat,
                    Location = new Point(30, 320),
                    Size = new Size(panelContent.Width - 60, 45),
                    Font = new Font("Segoe UI", 11, FontStyle.Bold)
                };
                logoutBtn.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, logoutBtn.Width, logoutBtn.Height, 20, 20));
                logoutBtn.FlatAppearance.BorderSize = 0;
                logoutBtn.Click += btnLogout_Click;

                panelContent.Controls.Add(profileCard);
                panelContent.Controls.Add(logoutBtn);
            }
            catch (Exception ex)
            {
                AddEmptyState("Error", $"Error al cargar perfil: {ex.Message}");
            }
        }

        private void AddStatItem(Panel parent, string value, string label, int x, int y, int width)
        {
            Panel statPanel = new Panel
            {
                Location = new Point(x, y),
                Size = new Size(width, 50)
            };

            Label valueLabel = new Label
            {
                Text = value,
                Font = new Font("Segoe UI", 16, FontStyle.Bold),
                ForeColor = Color.White,
                AutoSize = true,
                Location = new Point((width - 30) / 2, 0)
            };

            Label labelControl = new Label
            {
                Text = label,
                Font = new Font("Segoe UI", 9),
                ForeColor = Color.FromArgb(156, 163, 175),
                AutoSize = true,
                Location = new Point((width - 50) / 2, 25)
            };

            statPanel.Controls.Add(valueLabel);
            statPanel.Controls.Add(labelControl);
            parent.Controls.Add(statPanel);
        }

        private void AddEmptyState(string title, string message)
        {
            Panel emptyPanel = new Panel
            {
                BackColor = Color.FromArgb(42, 46, 51),
                Location = new Point(30, 20),
                Size = new Size(panelContent.Width - 60, 200)
            };
            emptyPanel.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, emptyPanel.Width, emptyPanel.Height, 20, 20));

            Label titleLabel = new Label
            {
                Text = title,
                Font = new Font("Segoe UI", 14, FontStyle.Bold),
                ForeColor = Color.White,
                AutoSize = true,
                Location = new Point((emptyPanel.Width - 200) / 2, 60)
            };

            Label messageLabel = new Label
            {
                Text = message,
                Font = new Font("Segoe UI", 10),
                ForeColor = Color.FromArgb(156, 163, 175),
                AutoSize = true,
                Location = new Point((emptyPanel.Width - 250) / 2, 90)
            };

            emptyPanel.Controls.Add(titleLabel);
            emptyPanel.Controls.Add(messageLabel);
            panelContent.Controls.Add(emptyPanel);
        }

        private void AddHabitCard(Habit habit, ref int yPos)
        {
            Panel card = new Panel
            {
                BackColor = Color.FromArgb(42, 46, 51),
                Location = new Point(30, yPos),
                Size = new Size(panelContent.Width - 60, 180),
                Name = "habitCard"
            };
            card.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, card.Width, card.Height, 20, 20));

            string avatarLetter = habit.Username?[0].ToString().ToUpper() ?? "U";

            PictureBox avatar = new PictureBox
            {
                Size = new Size(44, 44),
                Location = new Point(20, 20),
                BackColor = Color.FromArgb(250, 204, 21),
                SizeMode = PictureBoxSizeMode.Zoom
            };
            avatar.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, 44, 44, 22, 22));
            Label avatarLabel = new Label
            {
                Text = avatarLetter,
                Font = new Font("Segoe UI", 16, FontStyle.Bold),
                ForeColor = Color.FromArgb(28, 31, 34),
                AutoSize = false,
                Size = avatar.Size,
                TextAlign = ContentAlignment.MiddleCenter
            };
            avatar.Controls.Add(avatarLabel);

            Label usernameLabel = new Label
            {
                Text = "@" + (habit.Username ?? "usuario"),
                Font = new Font("Segoe UI", 10, FontStyle.Bold),
                ForeColor = Color.White,
                Location = new Point(75, 22)
            };

            Label timeLabel = new Label
            {
                Text = ApiService.GetTimeAgo(habit.Created_at),
                Font = new Font("Segoe UI", 9),
                ForeColor = Color.FromArgb(156, 163, 175),
                Location = new Point(75, 42)
            };

            Label typeLabel = new Label
            {
                Text = habit.Habit_type ?? "Hábito",
                BackColor = Color.FromArgb(102, 126, 234),
                ForeColor = Color.White,
                Font = new Font("Segoe UI", 8, FontStyle.Bold),
                Location = new Point(card.Width - 100, 25),
                Size = new Size(80, 25),
                TextAlign = ContentAlignment.MiddleCenter
            };
            typeLabel.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, 80, 25, 10, 10));

            Label descLabel = new Label
            {
                Text = habit.Description ?? "Nuevo hábito completado!",
                Font = new Font("Segoe UI", 10),
                ForeColor = Color.FromArgb(209, 213, 219),
                Location = new Point(20, 80),
                Size = new Size(card.Width - 40, 80),
                AutoSize = true
            };

            Button likeBtn = new Button
            {
                Text = $"♥ {habit.Likes_count}",
                BackColor = Color.Transparent,
                ForeColor = Color.FromArgb(209, 213, 219),
                FlatStyle = FlatStyle.Flat,
                Location = new Point(20, card.Height - 40),
                Size = new Size(60, 30),
                Font = new Font("Segoe UI", 10)
            };
            likeBtn.FlatAppearance.BorderSize = 0;

            card.Controls.Add(avatar);
            card.Controls.Add(usernameLabel);
            card.Controls.Add(timeLabel);
            card.Controls.Add(typeLabel);
            card.Controls.Add(descLabel);
            card.Controls.Add(likeBtn);

            panelContent.Controls.Add(card);
            yPos += card.Height + 20;
        }

        private async void AddUserCard(User user, bool isFollowing, ref int yPos)
        {
            Panel userCard = new Panel
            {
                BackColor = Color.FromArgb(42, 46, 51),
                Location = new Point(30, yPos),
                Size = new Size(panelContent.Width - 60, 70),
                Name = "userCard"
            };
            userCard.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, userCard.Width, userCard.Height, 15, 15));

            string avatarLetter = user.Name?[0].ToString().ToUpper() ?? "U";

            PictureBox avatar = new PictureBox
            {
                Size = new Size(44, 44),
                Location = new Point(15, 13),
                BackColor = Color.FromArgb(102, 126, 234),
                SizeMode = PictureBoxSizeMode.Zoom
            };
            avatar.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, 44, 44, 22, 22));
            Label avatarLabel = new Label
            {
                Text = avatarLetter,
                Font = new Font("Segoe UI", 16, FontStyle.Bold),
                ForeColor = Color.White,
                AutoSize = false,
                Size = avatar.Size,
                TextAlign = ContentAlignment.MiddleCenter
            };
            avatar.Controls.Add(avatarLabel);

            Label nameLabel = new Label
            {
                Text = user.Name ?? "Usuario",
                Font = new Font("Segoe UI", 11, FontStyle.Bold),
                ForeColor = Color.White,
                Location = new Point(70, 15)
            };

            Label usernameLabel = new Label
            {
                Text = "@" + (user.Username ?? "user"),
                Font = new Font("Segoe UI", 9),
                ForeColor = Color.FromArgb(156, 163, 175),
                Location = new Point(70, 38)
            };

            Button followBtn = new Button
            {
                Text = isFollowing ? "Siguiendo" : "Seguir",
                Location = new Point(userCard.Width - 100, 18),
                Size = new Size(85, 35),
                Font = new Font("Segoe UI", 10, FontStyle.Bold),
                Tag = user.Id
            };
            followBtn.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, 85, 35, 15, 15));

            if (isFollowing)
            {
                followBtn.BackColor = Color.FromArgb(55, 59, 65);
                followBtn.ForeColor = Color.White;
            }
            else
            {
                followBtn.BackColor = Color.FromArgb(250, 204, 21);
                followBtn.ForeColor = Color.FromArgb(28, 31, 34);
            }
            followBtn.FlatStyle = FlatStyle.Flat;
            followBtn.FlatAppearance.BorderSize = 0;
            followBtn.Click += async (s, e) => await ToggleFollow(followBtn, user.Id);

            userCard.Controls.Add(avatar);
            userCard.Controls.Add(nameLabel);
            userCard.Controls.Add(usernameLabel);
            userCard.Controls.Add(followBtn);

            panelContent.Controls.Add(userCard);
            yPos += userCard.Height + 10;
        }

        private async Task ToggleFollow(Button button, int userId)
        {
            bool isFollowing = button.Text == "Siguiendo";
            bool success;

            if (isFollowing)
            {
                success = await _apiService.UnfollowUserAsync(ApiService.CurrentUser.Id, userId);
            }
            else
            {
                success = await _apiService.FollowUserAsync(ApiService.CurrentUser.Id, userId);
            }

            if (success)
            {
                if (isFollowing)
                {
                    button.Text = "Seguir";
                    button.BackColor = Color.FromArgb(250, 204, 21);
                    button.ForeColor = Color.FromArgb(28, 31, 34);
                }
                else
                {
                    button.Text = "Siguiendo";
                    button.BackColor = Color.FromArgb(55, 59, 65);
                    button.ForeColor = Color.White;
                }
            }
        }

        private void ClearButtonsHighlight()
        {
            btnNavHome.BackColor = Color.Transparent;
            btnNavHome.ForeColor = Color.FromArgb(209, 213, 219);

            btnNavHabits.BackColor = Color.Transparent;
            btnNavHabits.ForeColor = Color.FromArgb(209, 213, 219);

            btnNavDiscover.BackColor = Color.Transparent;
            btnNavDiscover.ForeColor = Color.FromArgb(209, 213, 219);

            btnNavProfile.BackColor = Color.Transparent;
            btnNavProfile.ForeColor = Color.FromArgb(209, 213, 219);
        }

        private void btnNavHome_Click(object sender, EventArgs e)
        {
            _ = LoadFeed();
        }

        private void btnNavHabits_Click(object sender, EventArgs e)
        {
            _ = LoadMyHabits();
        }

        private void btnNavDiscover_Click(object sender, EventArgs e)
        {
            _ = LoadDiscover();
        }

        private void btnNavProfile_Click(object sender, EventArgs e)
        {
            _ = LoadProfile();
        }

        private void btnLogout_Click(object sender, EventArgs e)
        {
            ApiService.CurrentUser = null;
            Login loginForm = new Login();
            loginForm.Show();
            this.Hide();
        }

        private void btnNewHabit_Click(object sender, EventArgs e)
        {
            OpenHabitModal();
        }

        private void OpenHabitModal()
        {
            Form modalOverlay = new Form
            {
                BackColor = Color.FromArgb(150, 0, 0, 0),
                Size = this.Size,
                StartPosition = FormStartPosition.Manual,
                Location = this.Location,
                FormBorderStyle = FormBorderStyle.None,
                TopMost = true
            };

            Panel modal = new Panel
            {
                BackColor = Color.FromArgb(42, 46, 51),
                Size = new Size(450, 500),
                Location = new Point((modalOverlay.Width - 450) / 2, (modalOverlay.Height - 500) / 2)
            };
            modal.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, modal.Width, modal.Height, 30, 30));

            Label titleLabel = new Label
            {
                Text = "Compartir Hábito",
                Font = new Font("Segoe UI", 16, FontStyle.Bold),
                ForeColor = Color.White,
                Location = new Point(30, 30)
            };

            Label closeLabel = new Label
            {
                Text = "X",
                Font = new Font("Segoe UI", 14, FontStyle.Bold),
                ForeColor = Color.FromArgb(209, 213, 219),
                Location = new Point(modal.Width - 50, 20),
                AutoSize = true,
                Cursor = Cursors.Hand
            };
            closeLabel.Click += (s, e) =>
            {
                modalOverlay.Close();
                modalOverlay.Dispose();
            };

            Label typeLabel = new Label
            {
                Text = "TIPO DE HÁBITO",
                Font = new Font("Segoe UI", 9, FontStyle.Bold),
                ForeColor = Color.FromArgb(209, 213, 219),
                Location = new Point(30, 80)
            };

            ComboBox typeCombo = new ComboBox
            {
                BackColor = Color.FromArgb(55, 59, 65),
                ForeColor = Color.White,
                Location = new Point(30, 105),
                Size = new Size(modal.Width - 60, 35),
                FlatStyle = FlatStyle.Flat,
                Font = new Font("Segoe UI", 11)
            };
            typeCombo.Items.AddRange(new string[] { "Ejercicio", "Lectura", "Meditación", "Estudio", "Comida Saludable", "Otro" });
            typeCombo.SelectedIndex = 0;

            Label descLabel = new Label
            {
                Text = "DESCRIPCIÓN",
                Font = new Font("Segoe UI", 9, FontStyle.Bold),
                ForeColor = Color.FromArgb(209, 213, 219),
                Location = new Point(30, 155)
            };

            TextBox descText = new TextBox
            {
                BackColor = Color.FromArgb(55, 59, 65),
                ForeColor = Color.White,
                Location = new Point(30, 180),
                Size = new Size(modal.Width - 60, 100),
                Multiline = true,
                BorderStyle = BorderStyle.None,
                Font = new Font("Segoe UI", 11)
            };
            descText.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, descText.Width, descText.Height, 15, 15));

            Button shareBtn = new Button
            {
                Text = "Compartir",
                BackColor = Color.FromArgb(250, 204, 21),
                ForeColor = Color.FromArgb(28, 31, 34),
                Location = new Point(30, modal.Height - 80),
                Size = new Size(modal.Width - 60, 45),
                Font = new Font("Segoe UI", 11, FontStyle.Bold),
                FlatStyle = FlatStyle.Flat,
                FlatAppearance = { BorderSize = 0 }
            };
            shareBtn.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, shareBtn.Width, shareBtn.Height, 20, 20));
            shareBtn.Click += async (s, e) =>
            {
                shareBtn.Enabled = false;
                shareBtn.Text = "Compartiendo...";

                try
                {
                    bool success = await _apiService.CreateHabitAsync(
                        ApiService.CurrentUser.Id,
                        descText.Text,
                        _habitImageBase64,
                        typeCombo.SelectedItem?.ToString() ?? "Otro"
                    );

                    if (success)
                    {
                        MessageBox.Show("¡Hábito compartido!", "Éxito", MessageBoxButtons.OK, MessageBoxIcon.Information);
                        modalOverlay.Close();
                        modalOverlay.Dispose();
                        _ = LoadFeed();
                    }
                    else
                    {
                        MessageBox.Show("Error al compartir hábito.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    }
                }
                catch (Exception ex)
                {
                    MessageBox.Show($"Error: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
                finally
                {
                    shareBtn.Enabled = true;
                    shareBtn.Text = "Compartir";
                }
            };

            modal.Controls.Add(titleLabel);
            modal.Controls.Add(closeLabel);
            modal.Controls.Add(typeLabel);
            modal.Controls.Add(typeCombo);
            modal.Controls.Add(descLabel);
            modal.Controls.Add(descText);
            modal.Controls.Add(shareBtn);

            modalOverlay.Controls.Add(modal);
            modalOverlay.ShowDialog();
        }
    }
}
