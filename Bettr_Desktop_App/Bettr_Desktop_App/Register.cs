using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Bettr_Desktop_App
{
    public partial class Register : Form
    {
        public Register()
        {
            InitializeComponent();
        }

        private void lblBackToLogin_Click(object sender, EventArgs e)
        {
            Login loginForm = new Login();
            loginForm.Show();
            this.Hide();
        }

        private void btnRegister_Click(object sender, EventArgs e)
        {
            // For now, go back to login after "registering"
            MessageBox.Show("¡Cuenta creada con éxito! Por favor, inicia sesión.", "Registro", MessageBoxButtons.OK, MessageBoxIcon.Information);
            lblBackToLogin_Click(sender, e);
        }
    }
}
