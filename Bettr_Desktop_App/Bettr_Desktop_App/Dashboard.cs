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
            this.FormBorderStyle = FormBorderStyle.FixedSingle;
            this.ControlBox = false;
            this.MaximizeBox = false;
            this.MinimizeBox = true;
            this.Size = new Size(1200, 800);
            this.StartPosition = FormStartPosition.CenterScreen;
            this.BackColor = Color.FromArgb(28, 31, 34);
            CreateWindowControls();
            LoadIcon();
        }

        private void CreateWindowControls()
        {
            Panel titleBar = new Panel
            {
                Size = new Size(this.Width, 40),
                Location = new Point(0, 0),
                BackColor = Color.FromArgb(28, 31, 34)
            };

            Button btnMinimize = new Button
            {
                Text = "─",
                Size = new Size(50, 40),
                Location = new Point(this.Width - 110, 0),
                FlatStyle = FlatStyle.Flat,
                BackColor = Color.FromArgb(42, 46, 51),
                ForeColor = Color.White,
                FlatAppearance = { BorderSize = 0 },
                Cursor = Cursors.Hand
            };
            btnMinimize.Click += (s, e) => this.WindowState = FormWindowState.Minimized;

            Button btnClose = new Button
            {
                Text = "✕",
                Size = new Size(50, 40),
                Location = new Point(this.Width - 50, 0),
                FlatStyle = FlatStyle.Flat,
                BackColor = Color.FromArgb(239, 68, 68),
                ForeColor = Color.White,
                FlatAppearance = { BorderSize = 0 },
                Cursor = Cursors.Hand
            };
            btnClose.Click += (s, e) => Application.Exit();

            titleBar.Controls.Add(btnMinimize);
            titleBar.Controls.Add(btnClose);
            this.Controls.Add(titleBar);

            this.Resize += (s, e) => {
                titleBar.Size = new Size(this.Width, 40);
                btnMinimize.Location = new Point(this.Width - 110, 0);
                btnClose.Location = new Point(this.Width - 50, 0);
            };
        }

        private void LoadIcon()
        {
            try
            {
                string iconPath = System.IO.Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "Resources", "app.ico");
                if (System.IO.File.Exists(iconPath))
                {
                    using (var fs = new System.IO.FileStream(iconPath, System.IO.FileMode.Open, System.IO.FileAccess.Read))
                    {
                        this.Icon = new Icon(fs);
                    }
                    picSidebarLogo.Image = Image.FromFile(iconPath);
                }
            }
            catch { }
        }

        private async void Dashboard_Load(object sender, EventArgs e)
        {
            panelContent.AutoScroll = true;
            panelContent.HorizontalScroll.Enabled = false;
            panelContent.HorizontalScroll.Visible = false;
            panelContent.VerticalScroll.Enabled = false;
            panelContent.VerticalScroll.Visible = false;

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
                if (ApiService.CurrentUser == null)
                {
                    AddEmptyState("Error", "Usuario no encontrado");
                    return;
                }
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
                        yPos = await AddHabitCard(habit, yPos);
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
                if (ApiService.CurrentUser == null)
                {
                    AddEmptyState("Error", "Usuario no encontrado");
                    return;
                }

                int currentUserId = ApiService.CurrentUser.Id;
                Console.WriteLine($"Loading habits for user ID: {currentUserId}");
                List<Habit> habits = await _apiService.GetUserHabitsAsync(currentUserId);
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
                        yPos = await AddHabitCard(habit, yPos);
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
                Size = new Size(panelContent.Width - 60, 45),
                Font = new Font("Segoe UI", 12),
                BorderStyle = BorderStyle.FixedSingle,
                Text = "Buscar usuarios..."
            };
            searchBox.ForeColor = Color.FromArgb(156, 163, 175);
            searchBox.GotFocus += (s, e) => {
                if (searchBox.Text == "Buscar usuarios...")
                {
                    searchBox.Text = "";
                    searchBox.ForeColor = Color.White;
                }
            };
            searchBox.LostFocus += (s, e) => {
                if (string.IsNullOrEmpty(searchBox.Text))
                {
                    searchBox.Text = "Buscar usuarios...";
                    searchBox.ForeColor = Color.FromArgb(156, 163, 175);
                }
            };
            searchBox.TextChanged += async (s, e) => {
                if (searchBox.Text != "Buscar usuarios...")
                    await SearchUsers(searchBox.Text);
            };
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

            if (ApiService.CurrentUser == null)
                return;

            try
            {
                List<User> users = await _apiService.SearchUsersAsync(query);
                if (users != null)
                {
                    users = users.Where(u => u.Id != ApiService.CurrentUser.Id).ToList();

                    int yPos = searchBoxHeight + 20;
                    foreach (var user in users)
                    {
                        bool isFollowing = await _apiService.IsFollowingAsync(ApiService.CurrentUser.Id, user.Id);
                        yPos = await AddUserCard(user, isFollowing, yPos);
                    }
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

                if (ApiService.CurrentUser == null)
                {
                    AddEmptyState("Error", "No se pudo cargar el perfil");
                    return;
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

                int statsY = 230;
                int statWidth = (profileCard.Width - 60) / 3;

                AddStatItem(profileCard, stats["habits"].ToString(), "Hábitos", 30, statsY, statWidth);
                AddStatItem(profileCard, stats["followers"].ToString(), "Seguidores", 30 + statWidth, statsY, statWidth);
                AddStatItem(profileCard, stats["following"].ToString(), "Siguiendo", 30 + statWidth * 2, statsY, statWidth);

                profileCard.Controls.Add(avatar);
                profileCard.Controls.Add(nameLabel);
                profileCard.Controls.Add(usernameLabel);

                if (!string.IsNullOrEmpty(ApiService.CurrentUser.Description))
                {
                    Label descLabel = new Label
                    {
                        Text = ApiService.CurrentUser.Description,
                        Font = new Font("Segoe UI", 10),
                        ForeColor = Color.FromArgb(156, 163, 175),
                        Location = new Point(30, 195),
                        Size = new Size(profileCard.Width - 60, 60),
                        AutoSize = false
                    };
                    profileCard.Controls.Add(descLabel);
                }

                Button editProfileBtn = new Button
                {
                    Text = "Editar Perfil",
                    BackColor = Color.FromArgb(55, 59, 65),
                    ForeColor = Color.White,
                    FlatStyle = FlatStyle.Flat,
                    Location = new Point(30, 310),
                    Size = new Size((panelContent.Width - 70) / 2, 40),
                    Font = new Font("Segoe UI", 10, FontStyle.Bold)
                };
                editProfileBtn.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, editProfileBtn.Width, editProfileBtn.Height, 15, 15));
                editProfileBtn.FlatAppearance.BorderSize = 0;
                editProfileBtn.Click += (s, e) => OpenEditProfileModal();

                Button logoutBtn = new Button
                {
                    Text = "Cerrar Sesión",
                    BackColor = Color.FromArgb(239, 68, 68),
                    ForeColor = Color.White,
                    FlatStyle = FlatStyle.Flat,
                    Location = new Point(35 + editProfileBtn.Width, 310),
                    Size = new Size((panelContent.Width - 70) / 2, 40),
                    Font = new Font("Segoe UI", 10, FontStyle.Bold)
                };
                logoutBtn.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, logoutBtn.Width, logoutBtn.Height, 15, 15));
                logoutBtn.FlatAppearance.BorderSize = 0;
                logoutBtn.Click += btnLogout_Click;

                Panel statsPanel = new Panel
                {
                    Location = new Point(30, 360),
                    Size = new Size(panelContent.Width - 60, 60)
                };

                //Button followersBtn = new Button
                //{
                //    Text = $"{stats["followers"]} Seguidores",
                //    BackColor = Color.Transparent,
                //    ForeColor = Color.White,
                //    FlatStyle = FlatStyle.Flat,
                //    Location = new Point(0, 0),
                //    Size = new Size((statsPanel.Width - 20) / 2, 50),
                //    Font = new Font("Segoe UI", 10, FontStyle.Bold),
                //    Tag = "followers"
                //};
                //followersBtn.FlatAppearance.BorderSize = 0;
                //followersBtn.Click += (s, e) => ShowFollowersFollowing("followers");

                //Button followingBtn = new Button
                //{
                //    Text = $"{stats["following"]} Siguiendo",
                //    BackColor = Color.Transparent,
                //    ForeColor = Color.White,
                //    FlatStyle = FlatStyle.Flat,
                //    Location = new Point(10 + followersBtn.Width, 0),
                //    Size = new Size((statsPanel.Width - 20) / 2, 50),
                //    Font = new Font("Segoe UI", 10, FontStyle.Bold),
                //    Tag = "following"
                //};
                //followingBtn.FlatAppearance.BorderSize = 0;
                //followingBtn.Click += (s, e) => ShowFollowersFollowing("following");

                //statsPanel.Controls.Add(followersBtn);
                //statsPanel.Controls.Add(followingBtn);

                panelContent.Controls.Add(profileCard);
                panelContent.Controls.Add(editProfileBtn);
                panelContent.Controls.Add(logoutBtn);
                panelContent.Controls.Add(statsPanel);
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

        private async Task<int> AddHabitCard(Habit habit, int yPos)
        {
            bool isLiked = false;
            if (ApiService.CurrentUser != null)
            {
                isLiked = await _apiService.IsLikedAsync(habit.Id, ApiService.CurrentUser.Id);
            }

            int cardHeight = 180;
            if (!string.IsNullOrEmpty(habit.Image_url) && habit.Image_url.StartsWith("data:image"))
            {
                cardHeight = 330;
            }

            Panel card = new Panel
            {
                BackColor = Color.FromArgb(42, 46, 51),
                Location = new Point(30, yPos),
                Size = new Size(panelContent.Width - 60, cardHeight),
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

            LinkLabel usernameLabel = new LinkLabel
            {
                Text = "@" + (habit.Username ?? "usuario"),
                Font = new Font("Segoe UI", 10, FontStyle.Bold),
                ForeColor = Color.White,
                Location = new Point(75, 22),
                AutoSize = true,
                LinkBehavior = LinkBehavior.HoverUnderline
            };
            usernameLabel.LinkClicked += async (s, e) =>
            {
                await LoadOtherUserProfile(habit.User_id);
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

            int imageY = 80;
            if (!string.IsNullOrEmpty(habit.Image_url) && habit.Image_url.StartsWith("data:image"))
            {
                PictureBox habitImage = new PictureBox
                {
                    Location = new Point(20, 80),
                    Size = new Size(card.Width - 40, 140),
                    SizeMode = PictureBoxSizeMode.Zoom,
                    BackColor = Color.FromArgb(55, 59, 65)
                };
                habitImage.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, habitImage.Width, habitImage.Height, 15, 15));
                try
                {
                    habitImage.Image = Base64ToImage(habit.Image_url);
                }
                catch { }
                card.Controls.Add(habitImage);
                imageY = 230;
            }

            Label descLabel = new Label
            {
                Text = habit.Description ?? "Nuevo hábito completado!",
                Font = new Font("Segoe UI", 10),
                ForeColor = Color.FromArgb(209, 213, 219),
                Location = new Point(20, imageY),
                Size = new Size(card.Width - 40, 40),
                AutoSize = true
            };

            Button likeBtn = new Button
            {
                Text = $"♥ {habit.Likes_count}",
                BackColor = Color.Transparent,
                FlatStyle = FlatStyle.Flat,
                Location = new Point(20, card.Height - 40),
                Size = new Size(60, 30),
                Font = new Font("Segoe UI", 10),
                Tag = new { HabitId = habit.Id, IsLiked = isLiked, LikesCount = habit.Likes_count }
            };
            likeBtn.ForeColor = isLiked ? Color.FromArgb(239, 68, 68) : Color.FromArgb(209, 213, 219);
            likeBtn.FlatAppearance.BorderSize = 0;
            likeBtn.Click += async (s, e) =>
            {
                var data = (dynamic)likeBtn.Tag;
                bool currentlyLiked = data.IsLiked;
                int currentLikes = data.LikesCount;

                bool success;
                if (currentlyLiked)
                {
                    success = await _apiService.UnlikeHabitAsync(habit.Id, ApiService.CurrentUser.Id);
                    if (success)
                    {
                        likeBtn.Text = $"♥ {currentLikes - 1}";
                        likeBtn.ForeColor = Color.FromArgb(209, 213, 219);
                        data.IsLiked = false;
                        data.LikesCount = currentLikes - 1;
                    }
                }
                else
                {
                    success = await _apiService.LikeHabitAsync(habit.Id, ApiService.CurrentUser.Id);
                    if (success)
                    {
                        likeBtn.Text = $"♥ {currentLikes + 1}";
                        likeBtn.ForeColor = Color.FromArgb(239, 68, 68);
                        data.IsLiked = true;
                        data.LikesCount = currentLikes + 1;
                    }
                }
            };

            card.Controls.Add(avatar);
            card.Controls.Add(usernameLabel);
            card.Controls.Add(timeLabel);
            card.Controls.Add(typeLabel);
            card.Controls.Add(descLabel);
            card.Controls.Add(likeBtn);

            panelContent.Controls.Add(card);
            return yPos + card.Height + 20;
        }

        private async Task<int> AddUserCard(User user, bool isFollowing, int yPos)
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
            return yPos + userCard.Height + 10;
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
                BackColor = Color.FromArgb(30, 30, 30),
                Size = this.Size,
                StartPosition = FormStartPosition.Manual,
                Location = this.Location,
                FormBorderStyle = FormBorderStyle.None,
                TopMost = true
            };

            Panel modal = new Panel
            {
                BackColor = Color.FromArgb(42, 46, 51),
                Size = new Size(450, 580),
                Location = new Point((modalOverlay.Width - 450) / 2, (modalOverlay.Height - 580) / 2)
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

            Label imageLabel = new Label
            {
                Text = "IMAGEN (opcional)",
                Font = new Font("Segoe UI", 9, FontStyle.Bold),
                ForeColor = Color.FromArgb(209, 213, 219),
                Location = new Point(30, 155)
            };

            Button selectImageBtn = new Button
            {
                Text = "Seleccionar Imagen",
                BackColor = Color.FromArgb(55, 59, 65),
                ForeColor = Color.White,
                Location = new Point(30, 180),
                Size = new Size(modal.Width - 60, 35),
                FlatStyle = FlatStyle.Flat,
                Font = new Font("Segoe UI", 10)
            };
            selectImageBtn.FlatAppearance.BorderSize = 0;
            selectImageBtn.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, selectImageBtn.Width, selectImageBtn.Height, 10, 10));

            PictureBox imagePreview = new PictureBox
            {
                Location = new Point(30, 225),
                Size = new Size(modal.Width - 60, 100),
                SizeMode = PictureBoxSizeMode.Zoom,
                BackColor = Color.FromArgb(55, 59, 65),
                Visible = false
            };
            imagePreview.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, imagePreview.Width, imagePreview.Height, 10, 10));

            selectImageBtn.Click += (s, e) =>
            {
                using (OpenFileDialog openFile = new OpenFileDialog())
                {
                    openFile.Filter = "Archivos de imagen|*.jpg;*.jpeg;*.png;*.gif;*.bmp";
                    if (openFile.ShowDialog() == DialogResult.OK)
                    {
                        try
                        {
                            byte[] imageBytes = File.ReadAllBytes(openFile.FileName);
                            _habitImageBase64 = "data:image/jpeg;base64," + Convert.ToBase64String(imageBytes);
                            imagePreview.Image = Image.FromFile(openFile.FileName);
                            imagePreview.Visible = true;
                        }
                        catch (Exception ex)
                        {
                            MessageBox.Show("Error al cargar imagen: " + ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                        }
                    }
                }
            };

            Label descLabel = new Label
            {
                Text = "DESCRIPCIÓN",
                Font = new Font("Segoe UI", 9, FontStyle.Bold),
                ForeColor = Color.FromArgb(209, 213, 219),
                Location = new Point(30, 340)
            };

            TextBox descText = new TextBox
            {
                BackColor = Color.FromArgb(55, 59, 65),
                ForeColor = Color.White,
                Location = new Point(30, 365),
                Size = new Size(modal.Width - 60, 80),
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
                Location = new Point(30, 465),
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
            modal.Controls.Add(imageLabel);
            modal.Controls.Add(selectImageBtn);
            modal.Controls.Add(imagePreview);
            modal.Controls.Add(descLabel);
            modal.Controls.Add(descText);
            modal.Controls.Add(shareBtn);

            modalOverlay.Controls.Add(modal);
            modalOverlay.ShowDialog();
        }

        private async void ShowFollowersFollowing(string type)
        {
            Form modalOverlay = new Form
            {
                BackColor = Color.FromArgb(30, 30, 30),
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

            string titleText = type == "followers" ? "Seguidores" : "Siguiendo";

            Label titleLabel = new Label
            {
                Text = titleText,
                Font = new Font("Segoe UI", 16, FontStyle.Bold),
                ForeColor = Color.White,
                Location = new Point(30, 20)
            };

            Label closeLabel = new Label
            {
                Text = "X",
                Font = new Font("Segoe UI", 14, FontStyle.Bold),
                ForeColor = Color.FromArgb(209, 213, 219),
                Location = new Point(modal.Width - 50, 15),
                AutoSize = true,
                Cursor = Cursors.Hand
            };
            closeLabel.Click += (s, e) =>
            {
                modalOverlay.Close();
                modalOverlay.Dispose();
            };

            Panel listPanel = new Panel
            {
                Location = new Point(20, 70),
                Size = new Size(modal.Width - 40, 380),
                AutoScroll = true,
                HorizontalScroll = { Enabled = false, Visible = false }
            };

            modal.Controls.Add(titleLabel);
            modal.Controls.Add(closeLabel);
            modal.Controls.Add(listPanel);

            modalOverlay.Controls.Add(modal);
            modalOverlay.ShowDialog();

            try
            {
                Label loadingLabel = new Label
                {
                    Text = "Cargando...",
                    ForeColor = Color.FromArgb(209, 213, 219),
                    AutoSize = true,
                    Location = new Point(10, 10)
                };
                listPanel.Controls.Add(loadingLabel);

                if (ApiService.CurrentUser == null)
                {
                    listPanel.Controls.Clear();
                    Label errorLabel = new Label
                    {
                        Text = "Usuario no encontrado",
                        ForeColor = Color.FromArgb(156, 163, 175),
                        AutoSize = true,
                        Location = new Point(10, 10)
                    };
                    listPanel.Controls.Add(errorLabel);
                    return;
                }

                List<User> users = type == "followers" 
                    ? await _apiService.GetFollowersAsync(ApiService.CurrentUser.Id)
                    : await _apiService.GetFollowingAsync(ApiService.CurrentUser.Id);

                listPanel.Controls.Clear();

                if (users == null || users.Count == 0)
                {
                    Label emptyLabel = new Label
                    {
                        Text = type == "followers" ? "No tienes seguidores" : "No sigues a nadie",
                        ForeColor = Color.FromArgb(156, 163, 175),
                        AutoSize = true,
                        Location = new Point(10, 10)
                    };
                    listPanel.Controls.Add(emptyLabel);
                }
                else
                {
                    int yPos = 10;
                    foreach (var user in users)
                    {
                        bool isFollowing = await _apiService.IsFollowingAsync(ApiService.CurrentUser.Id, user.Id);
                        yPos = AddUserCard(user, isFollowing, yPos, listPanel);
                    }
                }
            }
            catch (Exception ex)
            {
                listPanel.Controls.Clear();
                Label errorLabel = new Label
                {
                    Text = "Error: " + ex.Message,
                    ForeColor = Color.FromArgb(239, 68, 68),
                    AutoSize = true,
                    Location = new Point(10, 10)
                };
                listPanel.Controls.Add(errorLabel);
            }
        }

        private int AddUserCard(User user, bool isFollowing, int yPos, Panel parentPanel)
        {
            Panel userCard = new Panel
            {
                BackColor = Color.FromArgb(55, 59, 65),
                Location = new Point(0, yPos),
                Size = new Size(parentPanel.Width - 20, 70),
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

            parentPanel.Controls.Add(userCard);
            return yPos + userCard.Height + 10;
        }

        private void OpenEditProfileModal()
        {
            Form modalOverlay = new Form
            {
                BackColor = Color.FromArgb(30, 30, 30),
                Size = this.Size,
                StartPosition = FormStartPosition.Manual,
                Location = this.Location,
                FormBorderStyle = FormBorderStyle.None,
                TopMost = true
            };

            Panel modal = new Panel
            {
                BackColor = Color.FromArgb(42, 46, 51),
                Size = new Size(450, 400),
                Location = new Point((modalOverlay.Width - 450) / 2, (modalOverlay.Height - 400) / 2)
            };
            modal.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, modal.Width, modal.Height, 30, 30));

            Label titleLabel = new Label
            {
                Text = "Editar Perfil",
                Font = new Font("Segoe UI", 16, FontStyle.Bold),
                ForeColor = Color.White,
                Location = new Point(30, 25)
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

            Label avatarLabelText = new Label
            {
                Text = "AVATAR",
                Font = new Font("Segoe UI", 9, FontStyle.Bold),
                ForeColor = Color.FromArgb(209, 213, 219),
                Location = new Point(30, 70)
            };

            Button selectAvatarBtn = new Button
            {
                Text = "Cambiar Avatar",
                BackColor = Color.FromArgb(55, 59, 65),
                ForeColor = Color.White,
                Location = new Point(30, 95),
                Size = new Size(modal.Width - 60, 35),
                FlatStyle = FlatStyle.Flat,
                Font = new Font("Segoe UI", 10)
            };
            selectAvatarBtn.FlatAppearance.BorderSize = 0;
            selectAvatarBtn.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, selectAvatarBtn.Width, selectAvatarBtn.Height, 10, 10));

            string newAvatarBase64 = "";

            selectAvatarBtn.Click += (s, e) =>
            {
                using (OpenFileDialog openFile = new OpenFileDialog())
                {
                    openFile.Filter = "Archivos de imagen|*.jpg;*.jpeg;*.png;*.gif;*.bmp";
                    if (openFile.ShowDialog() == DialogResult.OK)
                    {
                        try
                        {
                            byte[] imageBytes = File.ReadAllBytes(openFile.FileName);
                            newAvatarBase64 = "data:image/jpeg;base64," + Convert.ToBase64String(imageBytes);
                            MessageBox.Show("Avatar seleccionado", "Éxito", MessageBoxButtons.OK, MessageBoxIcon.Information);
                        }
                        catch (Exception ex)
                        {
                            MessageBox.Show("Error: " + ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                        }
                    }
                }
            };

            Label descLabel = new Label
            {
                Text = "DESCRIPCIÓN",
                Font = new Font("Segoe UI", 9, FontStyle.Bold),
                ForeColor = Color.FromArgb(209, 213, 219),
                Location = new Point(30, 150)
            };

            TextBox descText = new TextBox
            {
                BackColor = Color.FromArgb(55, 59, 65),
                ForeColor = Color.White,
                Location = new Point(30, 175),
                Size = new Size(modal.Width - 60, 80),
                Multiline = true,
                BorderStyle = BorderStyle.None,
                Font = new Font("Segoe UI", 11),
                Text = ApiService.CurrentUser.Description ?? ""
            };
            descText.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, descText.Width, descText.Height, 15, 15));

            Button saveBtn = new Button
            {
                Text = "Guardar Cambios",
                BackColor = Color.FromArgb(250, 204, 21),
                ForeColor = Color.FromArgb(28, 31, 34),
                Location = new Point(30, 280),
                Size = new Size(modal.Width - 60, 45),
                Font = new Font("Segoe UI", 11, FontStyle.Bold),
                FlatStyle = FlatStyle.Flat,
                FlatAppearance = { BorderSize = 0 }
            };
            saveBtn.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, saveBtn.Width, saveBtn.Height, 20, 20));
            saveBtn.Click += async (s, e) =>
            {
                saveBtn.Enabled = false;
                saveBtn.Text = "Guardando...";

                try
                {
                    string avatarToSave = string.IsNullOrEmpty(newAvatarBase64) 
                        ? ApiService.CurrentUser.Avatar 
                        : newAvatarBase64;

                    bool success = await _apiService.UpdateProfileAsync(
                        ApiService.CurrentUser.Id,
                        descText.Text,
                        avatarToSave
                    );

                    if (success)
                    {
                        MessageBox.Show("Perfil actualizado!", "Éxito", MessageBoxButtons.OK, MessageBoxIcon.Information);
                        modalOverlay.Close();
                        modalOverlay.Dispose();
                        _ = LoadProfile();
                    }
                    else
                    {
                        MessageBox.Show("Error al actualizar perfil.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    }
                }
                catch (Exception ex)
                {
                    MessageBox.Show($"Error: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
                finally
                {
                    saveBtn.Enabled = true;
                    saveBtn.Text = "Guardar Cambios";
                }
            };

            modal.Controls.Add(titleLabel);
            modal.Controls.Add(closeLabel);
            modal.Controls.Add(avatarLabelText);
            modal.Controls.Add(selectAvatarBtn);
            modal.Controls.Add(descLabel);
            modal.Controls.Add(descText);
            modal.Controls.Add(saveBtn);

            modalOverlay.Controls.Add(modal);
            modalOverlay.ShowDialog();
        }

        private async Task LoadOtherUserProfile(int userId)
        {
            _currentPage = "otherprofile";
            lblPageTitle.Text = "Perfil";
            ClearButtonsHighlight();

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
                User user = await _apiService.GetUserByIdAsync(userId);
                if (user == null)
                {
                    panelContent.Controls.Clear();
                    AddEmptyState("Error", "Usuario no encontrado");
                    return;
                }

                Dictionary<string, int> stats = await _apiService.GetUserStatsAsync(userId);
                bool isFollowing = await _apiService.IsFollowingAsync(ApiService.CurrentUser.Id, userId);

                panelContent.Controls.Clear();

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

                if (!string.IsNullOrEmpty(user.Avatar) && user.Avatar.StartsWith("data:image"))
                {
                    avatar.Image = Base64ToImage(user.Avatar);
                }
                else
                {
                    Label avatarLetter = new Label
                    {
                        Text = user.Name?[0].ToString().ToUpper() ?? "U",
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
                    Text = user.Name ?? "Usuario",
                    Font = new Font("Segoe UI", 16, FontStyle.Bold),
                    ForeColor = Color.White,
                    AutoSize = true,
                    Location = new Point((profileCard.Width - 200) / 2, 145)
                };

                Label usernameLabel = new Label
                {
                    Text = "@" + (user.Username ?? "user"),
                    Font = new Font("Segoe UI", 11),
                    ForeColor = Color.FromArgb(156, 163, 175),
                    AutoSize = true,
                    Location = new Point((profileCard.Width - 100) / 2, 170)
                };

                if (!string.IsNullOrEmpty(user.Description))
                {
                    Label descLabel = new Label
                    {
                        Text = user.Description,
                        Font = new Font("Segoe UI", 10),
                        ForeColor = Color.FromArgb(156, 163, 175),
                        Location = new Point(30, 195),
                        Size = new Size(profileCard.Width - 60, 50),
                        AutoSize = false
                    };
                    profileCard.Controls.Add(descLabel);
                }

                int statsY = 230;
                int statWidth = (profileCard.Width - 60) / 3;

                AddStatItem(profileCard, stats["followers"].ToString(), "Seguidores", 30, statsY, statWidth);
                AddStatItem(profileCard, stats["following"].ToString(), "Siguiendo", 30 + statWidth, statsY, statWidth);
                AddStatItem(profileCard, stats["habits"].ToString(), "Hábitos", 30 + statWidth * 2, statsY, statWidth);

                profileCard.Controls.Add(avatar);
                profileCard.Controls.Add(nameLabel);
                profileCard.Controls.Add(usernameLabel);

                Button followBtn = new Button
                {
                    Text = isFollowing ? "Siguiendo" : "Seguir",
                    BackColor = isFollowing ? Color.FromArgb(55, 59, 65) : Color.FromArgb(250, 204, 21),
                    ForeColor = isFollowing ? Color.White : Color.FromArgb(28, 31, 34),
                    FlatStyle = FlatStyle.Flat,
                    Location = new Point(30, 315),
                    Size = new Size(panelContent.Width - 60, 45),
                    Font = new Font("Segoe UI", 11, FontStyle.Bold)
                };
                followBtn.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, followBtn.Width, followBtn.Height, 20, 20));
                followBtn.FlatAppearance.BorderSize = 0;
                followBtn.Click += async (s, e) =>
                {
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
                        isFollowing = !isFollowing;
                        followBtn.Text = isFollowing ? "Siguiendo" : "Seguir";
                        followBtn.BackColor = isFollowing ? Color.FromArgb(55, 59, 65) : Color.FromArgb(250, 204, 21);
                        followBtn.ForeColor = isFollowing ? Color.White : Color.FromArgb(28, 31, 34);
                    }
                };

                Label habitsTitle = new Label
                {
                    Text = "Hábitos de " + user.Name,
                    Font = new Font("Segoe UI", 14, FontStyle.Bold),
                    ForeColor = Color.White,
                    Location = new Point(30, 375)
                };

                panelContent.Controls.Add(profileCard);
                panelContent.Controls.Add(followBtn);
                panelContent.Controls.Add(habitsTitle);

                List<Habit> userHabits = await _apiService.GetUserHabitsAsync(userId);
                if (userHabits.Count == 0)
                {
                    Label noHabitsLabel = new Label
                    {
                        Text = "Este usuario no tiene hábitos todavía",
                        Font = new Font("Segoe UI", 10),
                        ForeColor = Color.FromArgb(156, 163, 175),
                        Location = new Point(30, 410)
                    };
                    panelContent.Controls.Add(noHabitsLabel);
                }
                else
                {
                    int yPos = 440;
                    foreach (var habit in userHabits)
                    {
                        yPos = await AddHabitCard(habit, yPos);
                    }
                }
            }
            catch (Exception ex)
            {
                panelContent.Controls.Clear();
                AddEmptyState("Error", $"Error al cargar perfil: {ex.Message}");
            }
        }
    }
}
