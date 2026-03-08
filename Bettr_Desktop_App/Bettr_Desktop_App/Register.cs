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
using Bettr_Desktop_App.Models;

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
        }

        private void Register_Load(object sender, EventArgs e)
        {
            panelContainer.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, panelContainer.Width, panelContainer.Height, 30, 30));
            btnRegister.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, btnRegister.Width, btnRegister.Height, 20, 20));

            // Rounded textboxes
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

        private async void btnRegister_Click(object sender, EventArgs e)
        {
            string username = txtUsername.Text.Trim();
            string email = txtEmail.Text.Trim();
            string password = txtPassword.Text.Trim();
            string confirmPassword = txtConfirmPassword.Text.Trim();

            if (string.IsNullOrEmpty(username) || string.IsNullOrEmpty(email) || string.IsNullOrEmpty(password))
            {
                MessageBox.Show("Por favor, completa todos los campos.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            if (password != confirmPassword)
            {
                MessageBox.Show("Las contraseñas no coinciden.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            btnRegister.Enabled = false;
            btnRegister.Text = "Registrando...";

            try
            {
                User newUser = new User
                {
                    Username = username,
                    Email = email,
                    Password_hash = password, // Debería hashearse en producción
                    Name = username // Por defecto usamos el username como nombre
                };

                bool success = await _apiService.RegisterAsync(newUser);

                if (success)
                {
                    MessageBox.Show("¡Cuenta creada con éxito! Por favor, inicia sesión.", "Registro", MessageBoxButtons.OK, MessageBoxIcon.Information);
                    lblBackToLogin_Click(sender, e);
                }
                else
                {
                    MessageBox.Show("Error al registrar el usuario. El nombre de usuario o email podrían estar en uso.", "Error de Registro", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error de conexión: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
            finally
            {
                btnRegister.Enabled = true;
                btnRegister.Text = "Crear Cuenta";
            }
        }
    }
}
