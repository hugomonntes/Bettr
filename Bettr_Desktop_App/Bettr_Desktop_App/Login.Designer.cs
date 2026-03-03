namespace Bettr_Desktop_App
{
    partial class Login
    {
        /// <summary>
        /// Variable del diseñador necesaria.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Limpiar los recursos que se estén usando.
        /// </summary>
        /// <param name="disposing">true si los recursos administrados se deben desechar; false en caso contrario.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Código generado por el Diseñador

        private void InitializeComponent()
        {
            this.panelContainer = new System.Windows.Forms.Panel();
            this.lblRegister = new System.Windows.Forms.Label();
            this.btnLogin = new System.Windows.Forms.Button();
            this.txtPassword = new System.Windows.Forms.TextBox();
            this.txtUsername = new System.Windows.Forms.TextBox();
            this.lblPassword = new System.Windows.Forms.Label();
            this.lblUsername = new System.Windows.Forms.Label();
            this.lblError = new System.Windows.Forms.Label();
            this.lblSubtitle = new System.Windows.Forms.Label();
            this.lblTitle = new System.Windows.Forms.Label();
            this.picLogo = new System.Windows.Forms.PictureBox();
            this.panelContainer.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.picLogo)).BeginInit();
            this.SuspendLayout();

            // 
            // panelContainer
            // 
            this.panelContainer.BackColor = System.Drawing.Color.White;
            this.panelContainer.Controls.Add(this.lblRegister);
            this.panelContainer.Controls.Add(this.btnLogin);
            this.panelContainer.Controls.Add(this.txtPassword);
            this.panelContainer.Controls.Add(this.txtUsername);
            this.panelContainer.Controls.Add(this.lblPassword);
            this.panelContainer.Controls.Add(this.lblUsername);
            this.panelContainer.Controls.Add(this.lblError);
            this.panelContainer.Controls.Add(this.lblSubtitle);
            this.panelContainer.Controls.Add(this.lblTitle);
            this.panelContainer.Controls.Add(this.picLogo);
            this.panelContainer.Location = new System.Drawing.Point(250, 80);
            this.panelContainer.Name = "panelContainer";
            this.panelContainer.Size = new System.Drawing.Size(400, 520);
            this.panelContainer.TabIndex = 0;

            // 
            // picLogo
            // 
            this.picLogo.SizeMode = System.Windows.Forms.PictureBoxSizeMode.Zoom;
            this.picLogo.Location = new System.Drawing.Point(125, 20);
            this.picLogo.Size = new System.Drawing.Size(150, 100);

            // 
            // lblTitle
            // 
            this.lblTitle.AutoSize = true;
            this.lblTitle.Font = new System.Drawing.Font("Segoe UI", 18F, System.Drawing.FontStyle.Bold);
            this.lblTitle.Location = new System.Drawing.Point(60, 130);
            this.lblTitle.Text = "Bienvenido de nuevo";

            // 
            // lblSubtitle
            // 
            this.lblSubtitle.AutoSize = true;
            this.lblSubtitle.Font = new System.Drawing.Font("Segoe UI", 10F);
            this.lblSubtitle.ForeColor = System.Drawing.Color.Gray;
            this.lblSubtitle.Location = new System.Drawing.Point(95, 170);
            this.lblSubtitle.Text = "Inicia sesión para continuar";

            // 
            // lblError
            // 
            this.lblError.AutoSize = true;
            this.lblError.ForeColor = System.Drawing.Color.Red;
            this.lblError.Location = new System.Drawing.Point(60, 200);
            this.lblError.Size = new System.Drawing.Size(0, 13);

            // 
            // lblUsername
            // 
            this.lblUsername.AutoSize = true;
            this.lblUsername.Location = new System.Drawing.Point(60, 230);
            this.lblUsername.Text = "Usuario";

            // 
            // txtUsername
            // 
            this.txtUsername.Location = new System.Drawing.Point(60, 250);
            this.txtUsername.Size = new System.Drawing.Size(280, 25);
            this.txtUsername.Font = new System.Drawing.Font("Segoe UI", 10F);

            // 
            // lblPassword
            // 
            this.lblPassword.AutoSize = true;
            this.lblPassword.Location = new System.Drawing.Point(60, 290);
            this.lblPassword.Text = "Contraseña";

            // 
            // txtPassword
            // 
            this.txtPassword.Location = new System.Drawing.Point(60, 310);
            this.txtPassword.Size = new System.Drawing.Size(280, 25);
            this.txtPassword.Font = new System.Drawing.Font("Segoe UI", 10F);
            this.txtPassword.UseSystemPasswordChar = true;

            // 
            // btnLogin
            // 
            this.btnLogin.BackColor = System.Drawing.Color.FromArgb(34, 197, 94);
            this.btnLogin.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnLogin.FlatAppearance.BorderSize = 0;
            this.btnLogin.Font = new System.Drawing.Font("Segoe UI", 11F, System.Drawing.FontStyle.Bold);
            this.btnLogin.ForeColor = System.Drawing.Color.White;
            this.btnLogin.Location = new System.Drawing.Point(60, 360);
            this.btnLogin.Size = new System.Drawing.Size(280, 45);
            this.btnLogin.Text = "Iniciar Sesión";
            this.btnLogin.UseVisualStyleBackColor = false;

            // 
            // lblRegister
            // 
            this.lblRegister.AutoSize = true;
            this.lblRegister.ForeColor = System.Drawing.Color.Gray;
            this.lblRegister.Location = new System.Drawing.Point(95, 430);
            this.lblRegister.Text = "¿No tienes cuenta? Regístrate";
            this.lblRegister.Cursor = System.Windows.Forms.Cursors.Hand;

            // 
            // Login
            // 
            this.BackColor = System.Drawing.Color.FromArgb(245, 245, 245);
            this.ClientSize = new System.Drawing.Size(900, 700);
            this.Controls.Add(this.panelContainer);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Bettr - Iniciar Sesión";

            this.panelContainer.ResumeLayout(false);
            this.panelContainer.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.picLogo)).EndInit();
            this.ResumeLayout(false);
        }

        #endregion

        private System.Windows.Forms.Panel panelContainer;
        private System.Windows.Forms.PictureBox picLogo;
        private System.Windows.Forms.Label lblTitle;
        private System.Windows.Forms.Label lblSubtitle;
        private System.Windows.Forms.Label lblError;
        private System.Windows.Forms.Label lblUsername;
        private System.Windows.Forms.TextBox txtUsername;
        private System.Windows.Forms.Label lblPassword;
        private System.Windows.Forms.TextBox txtPassword;
        private System.Windows.Forms.Button btnLogin;
        private System.Windows.Forms.Label lblRegister;
    }
}

