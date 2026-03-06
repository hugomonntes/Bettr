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
    public partial class Dashboard : Form
    {
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
        }

        private void Dashboard_Load(object sender, EventArgs e)
        {
            // Initial content loading
            LoadFeed();

            // Apply rounded corners to UI elements
            picUserAvatar.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, picUserAvatar.Width, picUserAvatar.Height, 44, 44));
            btnNewHabit.Region = Region.FromHrgn(CreateRoundRectRgn(0, 0, btnNewHabit.Width, btnNewHabit.Height, 20, 20));
        }

        private void LoadFeed()
        {
            lblPageTitle.Text = "Inicio";
        }

        private void btnLogout_Click(object sender, EventArgs e)
        {
            Login loginForm = new Login();
            loginForm.Show();
            this.Hide();
        }
    }
}
