using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using Bettr_Desktop_App.Api;

namespace Bettr_Desktop_App
{
    public partial class Register : Form
    {
        private readonly ApiService _apiService = new ApiService();

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

        public Register()
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
                    using (var fs = new System.IO.FileStream(iconPath, System.IO.FileMode.Open, System.IO.FileAccess.Read))
                    {
                        this.Icon = new Icon(fs);
                    }
                    picLogo.Image = Image.FromFile(iconPath);
                }
            }
            catch { }
        }

        private void Register_Load(object sender, EventArgs e)
        {
            panelContainer.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, panelContainer.Width, panelContainer.Height, 30, 30));
            btnRegister.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, btnRegister.Width, btnRegister.Height, 20, 20));

            txtName.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, txtName.Width, txtName.Height, 15, 15));
            txtUsername.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, txtUsername.Width, txtUsername.Height, 15, 15));
            txtEmail.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, txtEmail.Width, txtEmail.Height, 15, 15));
            txtPassword.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, txtPassword.Width, txtPassword.Height, 15, 15));
            txtConfirmPassword.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, txtConfirmPassword.Width, txtConfirmPassword.Height, 15, 15));
        }

        private void lblBackToLogin_Click(object sender, EventArgs e)
        {
            Login loginForm = new Login();
            loginForm.Show();
            this.Hide();
        }

        private void lblClose_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

        private void txtPassword_TextChanged(object sender, EventArgs e)
        {
            UpdatePasswordStrength();
        }

        private void UpdatePasswordStrength()
        {
            string password = txtPassword.Text;
            int strength = 0;

            if (password.Length >= 6) strength++;
            if (password.Length >= 8) strength++;
            if (System.Text.RegularExpressions.Regex.IsMatch(password, "[A-Z]")) strength++;
            if (System.Text.RegularExpressions.Regex.IsMatch(password, "[0-9]")) strength++;
            if (System.Text.RegularExpressions.Regex.IsMatch(password, "[^A-Za-z0-9]")) strength++;

            if (string.IsNullOrEmpty(password))
            {
                progressPasswordStrength.Value = 0;
                lblStrengthText.Text = "";
                lblStrengthText.ForeColor = Color.FromArgb(156, 163, 175);
            }
            else if (strength <= 2)
            {
                progressPasswordStrength.Value = 33;
                progressPasswordStrength.ForeColor = Color.FromArgb(239, 68, 68);
                lblStrengthText.Text = "Contraseña débil";
                lblStrengthText.ForeColor = Color.FromArgb(239, 68, 68);
            }
            else if (strength <= 3)
            {
                progressPasswordStrength.Value = 66;
                progressPasswordStrength.ForeColor = Color.FromArgb(245, 158, 11);
                lblStrengthText.Text = "Contraseña media";
                lblStrengthText.ForeColor = Color.FromArgb(245, 158, 11);
            }
            else
            {
                progressPasswordStrength.Value = 100;
                progressPasswordStrength.ForeColor = Color.FromArgb(16, 185, 129);
                lblStrengthText.Text = "Contraseña fuerte";
                lblStrengthText.ForeColor = Color.FromArgb(16, 185, 129);
            }
        }

        private async void btnRegister_Click(object sender, EventArgs e)
        {
            string name = txtName.Text.Trim();
            string username = txtUsername.Text.Trim();
            string email = txtEmail.Text.Trim();
            string password = txtPassword.Text.Trim();
            string confirmPassword = txtConfirmPassword.Text.Trim();

            lblError.Text = "";

            if (string.IsNullOrEmpty(name) || string.IsNullOrEmpty(username) || string.IsNullOrEmpty(email) || string.IsNullOrEmpty(password))
            {
                lblError.Text = "Por favor, completa todos los campos.";
                return;
            }

            if (password != confirmPassword)
            {
                lblError.Text = "Las contraseñas no coinciden.";
                return;
            }

            if (password.Length < 6)
            {
                lblError.Text = "La contraseña debe tener al menos 6 caracteres.";
                return;
            }

            btnRegister.Enabled = false;
            btnRegister.Text = "Registrando...";

            try
            {
                bool success = await _apiService.RegisterAsync(name, username, email, password);

                if (success)
                {
                    MessageBox.Show("¡Cuenta creada con éxito! Por favor, inicia sesión.", "Registro", MessageBoxButtons.OK, MessageBoxIcon.Information);
                    lblBackToLogin_Click(sender, e);
                }
                else
                {
                    lblError.Text = "Error al registrar. El nombre de usuario o email podrían estar en uso.";
                }
            }
            catch (Exception ex)
            {
                lblError.Text = $"Error de conexión: {ex.Message}";
            }
            finally
            {
                btnRegister.Enabled = true;
                btnRegister.Text = "Crear Cuenta";
            }
        }
    }
}
