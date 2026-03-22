using System;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;
using System.Threading.Tasks;
using System.Windows.Forms;
using Bettr_Desktop_App.Api;
using Bettr_Desktop_App.Models;

namespace Bettr_Desktop_App
{
    public partial class CompleteProfile : Form
    {
        private readonly ApiService _apiService = new ApiService();
        private User _user;
        private string _avatarBase64 = "";

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

        public CompleteProfile(User user)
        {
            InitializeComponent();
            _user = user;
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
                    picLogo.Image = Image.FromFile(iconPath);
                }
            }
            catch { }
        }

        private void CompleteProfile_Load(object sender, EventArgs e)
        {
            panelContainer.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, panelContainer.Width, panelContainer.Height, 30, 30));
            btnSave.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, btnSave.Width, btnSave.Height, 20, 20));
            picAvatar.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, picAvatar.Width, picAvatar.Height, 60, 60));
            txtDescription.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, txtDescription.Width, txtDescription.Height, 15, 15));

            UpdateAvatarPreview();
        }

        private void UpdateAvatarPreview()
        {
            if (_user != null && !string.IsNullOrEmpty(_user.Name))
            {
                lblAvatarLetter.Text = _user.Name[0].ToString().ToUpper();
            }
            else
            {
                lblAvatarLetter.Text = "U";
            }
        }

        private void picAvatar_Click(object sender, EventArgs e)
        {
            using (OpenFileDialog openFileDialog = new OpenFileDialog())
            {
                openFileDialog.Filter = "Image files (*.jpg, *.jpeg, *.png, *.gif)|*.jpg;*.jpeg;*.png;*.gif";
                openFileDialog.Title = "Seleccionar foto de perfil";

                if (openFileDialog.ShowDialog() == DialogResult.OK)
                {
                    try
                    {
                        Image image = Image.FromFile(openFileDialog.FileName);
                        _avatarBase64 = ImageToBase64(image);

                        picAvatar.Image = image;

                        lblAvatarLetter.Visible = false;
                    }
                    catch (Exception ex)
                    {
                        MessageBox.Show($"Error al cargar la imagen: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    }
                }
            }
        }

        private string ImageToBase64(Image image)
        {
            using (MemoryStream ms = new MemoryStream())
            {
                image.Save(ms, ImageFormat.Jpeg);
                byte[] imageBytes = ms.ToArray();
                return "data:image/jpeg;base64," + Convert.ToBase64String(imageBytes);
            }
        }

        private async void btnSave_Click(object sender, EventArgs e)
        {
            string description = txtDescription.Text.Trim();

            btnSave.Enabled = false;
            btnSave.Text = "Guardando...";

            try
            {
                bool success = await _apiService.UpdateProfileAsync(_user.Id, description, _avatarBase64);

                if (success)
                {
                    _user.Description = description;
                    _user.Avatar = _avatarBase64;
                    ApiService.CurrentUser = _user;

                    MessageBox.Show("¡Perfil actualizado!", "Éxito", MessageBoxButtons.OK, MessageBoxIcon.Information);

                    Dashboard dashboard = new Dashboard();
                    dashboard.Show();
                    this.Hide();
                }
                else
                {
                    MessageBox.Show("Error al guardar el perfil.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error de conexión: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
            finally
            {
                btnSave.Enabled = true;
                btnSave.Text = "Guardar y Continuar";
            }
        }

        private void lblSkip_Click(object sender, EventArgs e)
        {
            Dashboard dashboard = new Dashboard();
            dashboard.Show();
            this.Hide();
        }

        private void lblClose_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }
    }
}
