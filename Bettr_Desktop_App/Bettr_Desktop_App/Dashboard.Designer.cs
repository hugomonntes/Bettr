namespace Bettr_Desktop_App
{
    partial class Dashboard
    {
        private System.ComponentModel.IContainer components = null;

        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        private void InitializeComponent()
        {
            this.panelSidebar = new System.Windows.Forms.Panel();
            this.btnNavLogout = new System.Windows.Forms.Button();
            this.btnNewHabit = new System.Windows.Forms.Button();
            this.btnNavProfile = new System.Windows.Forms.Button();
            this.btnNavDiscover = new System.Windows.Forms.Button();
            this.btnNavHabits = new System.Windows.Forms.Button();
            this.btnNavHome = new System.Windows.Forms.Button();
            this.panelUserInfo = new System.Windows.Forms.Panel();
            this.lblUserHandle = new System.Windows.Forms.Label();
            this.lblUserName = new System.Windows.Forms.Label();
            this.picUserAvatar = new System.Windows.Forms.PictureBox();
            this.lblAvatarInitial = new System.Windows.Forms.Label();
            this.lblSidebarBrand = new System.Windows.Forms.Label();
            this.picSidebarLogo = new System.Windows.Forms.PictureBox();
            this.panelMain = new System.Windows.Forms.Panel();
            this.panelContent = new System.Windows.Forms.Panel();
            this.panelTopbar = new System.Windows.Forms.Panel();
            this.lblPageTitle = new System.Windows.Forms.Label();
            this.panelSidebar.SuspendLayout();
            this.panelUserInfo.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.picUserAvatar)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.picSidebarLogo)).BeginInit();
            this.panelMain.SuspendLayout();
            this.panelTopbar.SuspendLayout();
            this.SuspendLayout();
            // 
            // panelSidebar
            // 
            this.panelSidebar.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(28)))), ((int)(((byte)(31)))), ((int)(((byte)(34)))));
            this.panelSidebar.Controls.Add(this.btnNavLogout);
            this.panelSidebar.Controls.Add(this.btnNewHabit);
            this.panelSidebar.Controls.Add(this.btnNavProfile);
            this.panelSidebar.Controls.Add(this.btnNavDiscover);
            this.panelSidebar.Controls.Add(this.btnNavHabits);
            this.panelSidebar.Controls.Add(this.btnNavHome);
            this.panelSidebar.Controls.Add(this.panelUserInfo);
            this.panelSidebar.Controls.Add(this.lblSidebarBrand);
            this.panelSidebar.Controls.Add(this.picSidebarLogo);
            this.panelSidebar.Dock = System.Windows.Forms.DockStyle.Left;
            this.panelSidebar.Location = new System.Drawing.Point(0, 0);
            this.panelSidebar.Name = "panelSidebar";
            this.panelSidebar.Size = new System.Drawing.Size(260, 720);
            this.panelSidebar.TabIndex = 0;
            // 
            // btnNavLogout
            // 
            this.btnNavLogout.Dock = System.Windows.Forms.DockStyle.Bottom;
            this.btnNavLogout.FlatAppearance.BorderSize = 0;
            this.btnNavLogout.FlatAppearance.MouseOverBackColor = System.Drawing.Color.FromArgb(((int)(((byte)(239)))), ((int)(((byte)(68)))), ((int)(((byte)(68)))));
            this.btnNavLogout.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnNavLogout.Font = new System.Drawing.Font("Segoe UI", 11F, System.Drawing.FontStyle.Bold);
            this.btnNavLogout.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(209)))), ((int)(((byte)(213)))), ((int)(((byte)(219)))));
            this.btnNavLogout.Location = new System.Drawing.Point(0, 670);
            this.btnNavLogout.Name = "btnNavLogout";
            this.btnNavLogout.Size = new System.Drawing.Size(260, 50);
            this.btnNavLogout.TabIndex = 8;
            this.btnNavLogout.Text = "      Cerrar Sesión";
            this.btnNavLogout.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            this.btnNavLogout.UseVisualStyleBackColor = true;
            this.btnNavLogout.Click += new System.EventHandler(this.btnLogout_Click);
            // 
            // btnNewHabit
            // 
            this.btnNewHabit.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(250)))), ((int)(((byte)(204)))), ((int)(((byte)(21)))));
            this.btnNewHabit.FlatAppearance.BorderSize = 0;
            this.btnNewHabit.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnNewHabit.Font = new System.Drawing.Font("Segoe UI", 11F, System.Drawing.FontStyle.Bold);
            this.btnNewHabit.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(28)))), ((int)(((byte)(31)))), ((int)(((byte)(34)))));
            this.btnNewHabit.Location = new System.Drawing.Point(0, 604);
            this.btnNewHabit.Name = "btnNewHabit";
            this.btnNewHabit.Size = new System.Drawing.Size(260, 50);
            this.btnNewHabit.TabIndex = 7;
            this.btnNewHabit.Text = "+ Nuevo Hábito";
            this.btnNewHabit.UseVisualStyleBackColor = false;
            this.btnNewHabit.Click += new System.EventHandler(this.btnNewHabit_Click);
            // 
            // btnNavProfile
            // 
            this.btnNavProfile.Dock = System.Windows.Forms.DockStyle.Top;
            this.btnNavProfile.FlatAppearance.BorderSize = 0;
            this.btnNavProfile.FlatAppearance.MouseOverBackColor = System.Drawing.Color.FromArgb(((int)(((byte)(42)))), ((int)(((byte)(46)))), ((int)(((byte)(51)))));
            this.btnNavProfile.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnNavProfile.Font = new System.Drawing.Font("Segoe UI", 11F, System.Drawing.FontStyle.Bold);
            this.btnNavProfile.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(209)))), ((int)(((byte)(213)))), ((int)(((byte)(219)))));
            this.btnNavProfile.Location = new System.Drawing.Point(0, 250);
            this.btnNavProfile.Name = "btnNavProfile";
            this.btnNavProfile.Size = new System.Drawing.Size(260, 50);
            this.btnNavProfile.TabIndex = 6;
            this.btnNavProfile.Text = "      Perfil";
            this.btnNavProfile.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            this.btnNavProfile.UseVisualStyleBackColor = true;
            this.btnNavProfile.Click += new System.EventHandler(this.btnNavProfile_Click);
            // 
            // btnNavDiscover
            // 
            this.btnNavDiscover.Dock = System.Windows.Forms.DockStyle.Top;
            this.btnNavDiscover.FlatAppearance.BorderSize = 0;
            this.btnNavDiscover.FlatAppearance.MouseOverBackColor = System.Drawing.Color.FromArgb(((int)(((byte)(42)))), ((int)(((byte)(46)))), ((int)(((byte)(51)))));
            this.btnNavDiscover.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnNavDiscover.Font = new System.Drawing.Font("Segoe UI", 11F, System.Drawing.FontStyle.Bold);
            this.btnNavDiscover.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(209)))), ((int)(((byte)(213)))), ((int)(((byte)(219)))));
            this.btnNavDiscover.Location = new System.Drawing.Point(0, 200);
            this.btnNavDiscover.Name = "btnNavDiscover";
            this.btnNavDiscover.Size = new System.Drawing.Size(260, 50);
            this.btnNavDiscover.TabIndex = 5;
            this.btnNavDiscover.Text = "      Descubrir";
            this.btnNavDiscover.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            this.btnNavDiscover.UseVisualStyleBackColor = true;
            this.btnNavDiscover.Click += new System.EventHandler(this.btnNavDiscover_Click);
            // 
            // btnNavHabits
            // 
            this.btnNavHabits.Dock = System.Windows.Forms.DockStyle.Top;
            this.btnNavHabits.FlatAppearance.BorderSize = 0;
            this.btnNavHabits.FlatAppearance.MouseOverBackColor = System.Drawing.Color.FromArgb(((int)(((byte)(42)))), ((int)(((byte)(46)))), ((int)(((byte)(51)))));
            this.btnNavHabits.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnNavHabits.Font = new System.Drawing.Font("Segoe UI", 11F, System.Drawing.FontStyle.Bold);
            this.btnNavHabits.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(209)))), ((int)(((byte)(213)))), ((int)(((byte)(219)))));
            this.btnNavHabits.Location = new System.Drawing.Point(0, 150);
            this.btnNavHabits.Name = "btnNavHabits";
            this.btnNavHabits.Size = new System.Drawing.Size(260, 50);
            this.btnNavHabits.TabIndex = 4;
            this.btnNavHabits.Text = "      Mis Hábitos";
            this.btnNavHabits.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            this.btnNavHabits.UseVisualStyleBackColor = true;
            this.btnNavHabits.Click += new System.EventHandler(this.btnNavHabits_Click);
            // 
            // btnNavHome
            // 
            this.btnNavHome.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(250)))), ((int)(((byte)(204)))), ((int)(((byte)(21)))));
            this.btnNavHome.Dock = System.Windows.Forms.DockStyle.Top;
            this.btnNavHome.FlatAppearance.BorderSize = 0;
            this.btnNavHome.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnNavHome.Font = new System.Drawing.Font("Segoe UI", 11F, System.Drawing.FontStyle.Bold);
            this.btnNavHome.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(28)))), ((int)(((byte)(31)))), ((int)(((byte)(34)))));
            this.btnNavHome.Location = new System.Drawing.Point(0, 100);
            this.btnNavHome.Name = "btnNavHome";
            this.btnNavHome.Size = new System.Drawing.Size(260, 50);
            this.btnNavHome.TabIndex = 3;
            this.btnNavHome.Text = "      Inicio";
            this.btnNavHome.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            this.btnNavHome.UseVisualStyleBackColor = false;
            this.btnNavHome.Click += new System.EventHandler(this.btnNavHome_Click);
            // 
            // panelUserInfo
            // 
            this.panelUserInfo.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(42)))), ((int)(((byte)(46)))), ((int)(((byte)(51)))));
            this.panelUserInfo.Controls.Add(this.lblUserHandle);
            this.panelUserInfo.Controls.Add(this.lblUserName);
            this.panelUserInfo.Controls.Add(this.picUserAvatar);
            this.panelUserInfo.Controls.Add(this.lblAvatarInitial);
            this.panelUserInfo.Dock = System.Windows.Forms.DockStyle.Top;
            this.panelUserInfo.Location = new System.Drawing.Point(0, 0);
            this.panelUserInfo.Name = "panelUserInfo";
            this.panelUserInfo.Size = new System.Drawing.Size(260, 100);
            this.panelUserInfo.TabIndex = 2;
            // 
            // lblUserHandle
            // 
            this.lblUserHandle.AutoSize = true;
            this.lblUserHandle.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(156)))), ((int)(((byte)(163)))), ((int)(((byte)(175)))));
            this.lblUserHandle.Location = new System.Drawing.Point(75, 52);
            this.lblUserHandle.Name = "lblUserHandle";
            this.lblUserHandle.Size = new System.Drawing.Size(64, 13);
            this.lblUserHandle.TabIndex = 2;
            this.lblUserHandle.Text = "@username";
            // 
            // lblUserName
            // 
            this.lblUserName.AutoSize = true;
            this.lblUserName.Font = new System.Drawing.Font("Segoe UI", 10F, System.Drawing.FontStyle.Bold);
            this.lblUserName.ForeColor = System.Drawing.Color.White;
            this.lblUserName.Location = new System.Drawing.Point(75, 32);
            this.lblUserName.Name = "lblUserName";
            this.lblUserName.Size = new System.Drawing.Size(60, 19);
            this.lblUserName.TabIndex = 1;
            this.lblUserName.Text = "Usuario";
            // 
            // picUserAvatar
            // 
            this.picUserAvatar.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(250)))), ((int)(((byte)(204)))), ((int)(((byte)(21)))));
            this.picUserAvatar.Location = new System.Drawing.Point(20, 28);
            this.picUserAvatar.Name = "picUserAvatar";
            this.picUserAvatar.Size = new System.Drawing.Size(44, 44);
            this.picUserAvatar.SizeMode = System.Windows.Forms.PictureBoxSizeMode.Zoom;
            this.picUserAvatar.TabIndex = 0;
            this.picUserAvatar.TabStop = false;
            // 
            // lblAvatarInitial
            // 
            this.lblAvatarInitial.AutoSize = false;
            this.lblAvatarInitial.Font = new System.Drawing.Font("Segoe UI", 16F, System.Drawing.FontStyle.Bold);
            this.lblAvatarInitial.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(28)))), ((int)(((byte)(31)))), ((int)(((byte)(34)))));
            this.lblAvatarInitial.Location = new System.Drawing.Point(20, 28);
            this.lblAvatarInitial.Name = "lblAvatarInitial";
            this.lblAvatarInitial.Size = new System.Drawing.Size(44, 44);
            this.lblAvatarInitial.TabIndex = 3;
            this.lblAvatarInitial.Text = "U";
            this.lblAvatarInitial.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // lblSidebarBrand
            // 
            this.lblSidebarBrand.AutoSize = true;
            this.lblSidebarBrand.Font = new System.Drawing.Font("Segoe UI", 18F, System.Drawing.FontStyle.Bold);
            this.lblSidebarBrand.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(250)))), ((int)(((byte)(204)))), ((int)(((byte)(21)))));
            this.lblSidebarBrand.Location = new System.Drawing.Point(70, 48);
            this.lblSidebarBrand.Name = "lblSidebarBrand";
            this.lblSidebarBrand.Size = new System.Drawing.Size(70, 32);
            this.lblSidebarBrand.TabIndex = 1;
            this.lblSidebarBrand.Text = "Bettr";
            // 
            // picSidebarLogo
            // 
            this.picSidebarLogo.Location = new System.Drawing.Point(20, 44);
            this.picSidebarLogo.Name = "picSidebarLogo";
            this.picSidebarLogo.Size = new System.Drawing.Size(40, 40);
            this.picSidebarLogo.SizeMode = System.Windows.Forms.PictureBoxSizeMode.Zoom;
            this.picSidebarLogo.TabIndex = 0;
            this.picSidebarLogo.TabStop = false;
            // 
            // panelMain
            // 
            this.panelMain.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(28)))), ((int)(((byte)(31)))), ((int)(((byte)(34)))));
            this.panelMain.Controls.Add(this.panelContent);
            this.panelMain.Controls.Add(this.panelTopbar);
            this.panelMain.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panelMain.Location = new System.Drawing.Point(260, 0);
            this.panelMain.Name = "panelMain";
            this.panelMain.Size = new System.Drawing.Size(740, 720);
            this.panelMain.TabIndex = 1;
            // 
            // panelContent
            // 
            this.panelContent.AutoScroll = true;
            this.panelContent.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panelContent.Location = new System.Drawing.Point(0, 70);
            this.panelContent.Name = "panelContent";
            this.panelContent.Padding = new System.Windows.Forms.Padding(30);
            this.panelContent.Size = new System.Drawing.Size(740, 650);
            this.panelContent.TabIndex = 1;
            // 
            // panelTopbar
            // 
            this.panelTopbar.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(28)))), ((int)(((byte)(31)))), ((int)(((byte)(34)))));
            this.panelTopbar.Controls.Add(this.lblPageTitle);
            this.panelTopbar.Dock = System.Windows.Forms.DockStyle.Top;
            this.panelTopbar.Location = new System.Drawing.Point(0, 0);
            this.panelTopbar.Name = "panelTopbar";
            this.panelTopbar.Size = new System.Drawing.Size(740, 70);
            this.panelTopbar.TabIndex = 0;
            // 
            // lblPageTitle
            // 
            this.lblPageTitle.AutoSize = true;
            this.lblPageTitle.Font = new System.Drawing.Font("Segoe UI", 18F, System.Drawing.FontStyle.Bold);
            this.lblPageTitle.ForeColor = System.Drawing.Color.White;
            this.lblPageTitle.Location = new System.Drawing.Point(30, 20);
            this.lblPageTitle.Name = "lblPageTitle";
            this.lblPageTitle.Size = new System.Drawing.Size(78, 32);
            this.lblPageTitle.TabIndex = 0;
            this.lblPageTitle.Text = "Inicio";
            // 
            // Dashboard
            // 
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(28)))), ((int)(((byte)(31)))), ((int)(((byte)(34)))));
            this.ClientSize = new System.Drawing.Size(1000, 720);
            this.Controls.Add(this.panelMain);
            this.Controls.Add(this.panelSidebar);
            this.Name = "Dashboard";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Bettr - Dashboard";
            this.Load += new System.EventHandler(this.Dashboard_Load);
            this.panelSidebar.ResumeLayout(false);
            this.panelSidebar.PerformLayout();
            this.panelUserInfo.ResumeLayout(false);
            this.panelUserInfo.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.picUserAvatar)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.picSidebarLogo)).EndInit();
            this.panelMain.ResumeLayout(false);
            this.panelTopbar.ResumeLayout(false);
            this.panelTopbar.PerformLayout();
            this.ResumeLayout(false);
        }

        private System.Windows.Forms.Panel panelSidebar;
        private System.Windows.Forms.Panel panelMain;
        private System.Windows.Forms.Panel panelTopbar;
        private System.Windows.Forms.Label lblPageTitle;
        private System.Windows.Forms.Panel panelContent;
        private System.Windows.Forms.PictureBox picSidebarLogo;
        private System.Windows.Forms.Label lblSidebarBrand;
        private System.Windows.Forms.Panel panelUserInfo;
        private System.Windows.Forms.PictureBox picUserAvatar;
        private System.Windows.Forms.Label lblUserName;
        private System.Windows.Forms.Label lblUserHandle;
        private System.Windows.Forms.Label lblAvatarInitial;
        private System.Windows.Forms.Button btnNavHome;
        private System.Windows.Forms.Button btnNavHabits;
        private System.Windows.Forms.Button btnNavDiscover;
        private System.Windows.Forms.Button btnNavProfile;
        private System.Windows.Forms.Button btnNewHabit;
        private System.Windows.Forms.Button btnNavLogout;
    }
}
