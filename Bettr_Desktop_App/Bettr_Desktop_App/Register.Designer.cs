namespace Bettr_Desktop_App
{
    partial class Register
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
            this.panelContainer = new System.Windows.Forms.Panel();
            this.picLogo = new System.Windows.Forms.PictureBox();
            this.lblTitle = new System.Windows.Forms.Label();
            this.lblSubtitle = new System.Windows.Forms.Label();
            this.lblError = new System.Windows.Forms.Label();
            this.lblName = new System.Windows.Forms.Label();
            this.txtName = new System.Windows.Forms.TextBox();
            this.lblUsername = new System.Windows.Forms.Label();
            this.txtUsername = new System.Windows.Forms.TextBox();
            this.lblEmail = new System.Windows.Forms.Label();
            this.txtEmail = new System.Windows.Forms.TextBox();
            this.lblPassword = new System.Windows.Forms.Label();
            this.txtPassword = new System.Windows.Forms.TextBox();
            this.progressPasswordStrength = new System.Windows.Forms.ProgressBar();
            this.lblStrengthText = new System.Windows.Forms.Label();
            this.lblConfirmPassword = new System.Windows.Forms.Label();
            this.txtConfirmPassword = new System.Windows.Forms.TextBox();
            this.btnRegister = new System.Windows.Forms.Button();
            this.lblBackToLogin = new System.Windows.Forms.Label();
            this.lblClose = new System.Windows.Forms.Label();
            this.panelContainer.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.picLogo)).BeginInit();
            this.SuspendLayout();
            // 
            // panelContainer
            // 
            this.panelContainer.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(42)))), ((int)(((byte)(46)))), ((int)(((byte)(51)))));
            this.panelContainer.Controls.Add(this.lblClose);
            this.panelContainer.Controls.Add(this.picLogo);
            this.panelContainer.Controls.Add(this.lblTitle);
            this.panelContainer.Controls.Add(this.lblSubtitle);
            this.panelContainer.Controls.Add(this.lblError);
            this.panelContainer.Controls.Add(this.lblName);
            this.panelContainer.Controls.Add(this.txtName);
            this.panelContainer.Controls.Add(this.lblUsername);
            this.panelContainer.Controls.Add(this.txtUsername);
            this.panelContainer.Controls.Add(this.lblEmail);
            this.panelContainer.Controls.Add(this.txtEmail);
            this.panelContainer.Controls.Add(this.lblPassword);
            this.panelContainer.Controls.Add(this.txtPassword);
            this.panelContainer.Controls.Add(this.progressPasswordStrength);
            this.panelContainer.Controls.Add(this.lblStrengthText);
            this.panelContainer.Controls.Add(this.lblConfirmPassword);
            this.panelContainer.Controls.Add(this.txtConfirmPassword);
            this.panelContainer.Controls.Add(this.btnRegister);
            this.panelContainer.Controls.Add(this.lblBackToLogin);
            this.panelContainer.Location = new System.Drawing.Point(250, 30);
            this.panelContainer.Name = "panelContainer";
            this.panelContainer.Size = new System.Drawing.Size(400, 700);
            this.panelContainer.TabIndex = 0;
            // 
            // lblClose
            // 
            this.lblClose.AutoSize = true;
            this.lblClose.Cursor = System.Windows.Forms.Cursors.Hand;
            this.lblClose.Font = new System.Drawing.Font("Segoe UI", 14F, System.Drawing.FontStyle.Bold);
            this.lblClose.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(209)))), ((int)(((byte)(213)))), ((int)(((byte)(219)))));
            this.lblClose.Location = new System.Drawing.Point(370, 10);
            this.lblClose.Name = "lblClose";
            this.lblClose.Size = new System.Drawing.Size(20, 25);
            this.lblClose.TabIndex = 14;
            this.lblClose.Text = "X";
            this.lblClose.Click += new System.EventHandler(this.lblClose_Click);
            // 
            // picLogo
            // 
            this.picLogo.Location = new System.Drawing.Point(140, 20);
            this.picLogo.Name = "picLogo";
            this.picLogo.Size = new System.Drawing.Size(120, 100);
            this.picLogo.SizeMode = System.Windows.Forms.PictureBoxSizeMode.Zoom;
            this.picLogo.TabIndex = 0;
            this.picLogo.TabStop = false;
            // 
            // lblTitle
            // 
            this.lblTitle.AutoSize = true;
            this.lblTitle.Font = new System.Drawing.Font("Segoe UI", 20F, System.Drawing.FontStyle.Bold);
            this.lblTitle.ForeColor = System.Drawing.Color.White;
            this.lblTitle.Location = new System.Drawing.Point(110, 130);
            this.lblTitle.Name = "lblTitle";
            this.lblTitle.Size = new System.Drawing.Size(179, 37);
            this.lblTitle.TabIndex = 1;
            this.lblTitle.Text = "Crear cuenta";
            // 
            // lblSubtitle
            // 
            this.lblSubtitle.AutoSize = true;
            this.lblSubtitle.Font = new System.Drawing.Font("Segoe UI", 10F);
            this.lblSubtitle.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(209)))), ((int)(((byte)(213)))), ((int)(((byte)(219)))));
            this.lblSubtitle.Location = new System.Drawing.Point(120, 170);
            this.lblSubtitle.Name = "lblSubtitle";
            this.lblSubtitle.Size = new System.Drawing.Size(164, 19);
            this.lblSubtitle.TabIndex = 2;
            this.lblSubtitle.Text = "Regístrate para comenzar";
            // 
            // lblError
            // 
            this.lblError.AutoSize = true;
            this.lblError.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(239)))), ((int)(((byte)(68)))), ((int)(((byte)(68)))));
            this.lblError.Location = new System.Drawing.Point(60, 200);
            this.lblError.Name = "lblError";
            this.lblError.Size = new System.Drawing.Size(0, 13);
            this.lblError.TabIndex = 3;
            // 
            // lblName
            // 
            this.lblName.Font = new System.Drawing.Font("Segoe UI", 9F, System.Drawing.FontStyle.Bold);
            this.lblName.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(209)))), ((int)(((byte)(213)))), ((int)(((byte)(219)))));
            this.lblName.Location = new System.Drawing.Point(60, 220);
            this.lblName.Name = "lblName";
            this.lblName.Size = new System.Drawing.Size(100, 23);
            this.lblName.TabIndex = 4;
            this.lblName.Text = "NOMBRE COMPLETO";
            // 
            // txtName
            // 
            this.txtName.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(55)))), ((int)(((byte)(59)))), ((int)(((byte)(65)))));
            this.txtName.BorderStyle = System.Windows.Forms.BorderStyle.None;
            this.txtName.Font = new System.Drawing.Font("Segoe UI", 12F);
            this.txtName.ForeColor = System.Drawing.Color.White;
            this.txtName.Location = new System.Drawing.Point(60, 245);
            this.txtName.Name = "txtName";
            this.txtName.Size = new System.Drawing.Size(280, 22);
            this.txtName.TabIndex = 5;
            // 
            // lblUsername
            // 
            this.lblUsername.Font = new System.Drawing.Font("Segoe UI", 9F, System.Drawing.FontStyle.Bold);
            this.lblUsername.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(209)))), ((int)(((byte)(213)))), ((int)(((byte)(219)))));
            this.lblUsername.Location = new System.Drawing.Point(60, 280);
            this.lblUsername.Name = "lblUsername";
            this.lblUsername.Size = new System.Drawing.Size(100, 23);
            this.lblUsername.TabIndex = 6;
            this.lblUsername.Text = "USUARIO";
            // 
            // txtUsername
            // 
            this.txtUsername.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(55)))), ((int)(((byte)(59)))), ((int)(((byte)(65)))));
            this.txtUsername.BorderStyle = System.Windows.Forms.BorderStyle.None;
            this.txtUsername.Font = new System.Drawing.Font("Segoe UI", 12F);
            this.txtUsername.ForeColor = System.Drawing.Color.White;
            this.txtUsername.Location = new System.Drawing.Point(60, 305);
            this.txtUsername.Name = "txtUsername";
            this.txtUsername.Size = new System.Drawing.Size(280, 22);
            this.txtUsername.TabIndex = 7;
            // 
            // lblEmail
            // 
            this.lblEmail.Font = new System.Drawing.Font("Segoe UI", 9F, System.Drawing.FontStyle.Bold);
            this.lblEmail.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(209)))), ((int)(((byte)(213)))), ((int)(((byte)(219)))));
            this.lblEmail.Location = new System.Drawing.Point(60, 340);
            this.lblEmail.Name = "lblEmail";
            this.lblEmail.Size = new System.Drawing.Size(100, 23);
            this.lblEmail.TabIndex = 8;
            this.lblEmail.Text = "EMAIL";
            // 
            // txtEmail
            // 
            this.txtEmail.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(55)))), ((int)(((byte)(59)))), ((int)(((byte)(65)))));
            this.txtEmail.BorderStyle = System.Windows.Forms.BorderStyle.None;
            this.txtEmail.Font = new System.Drawing.Font("Segoe UI", 12F);
            this.txtEmail.ForeColor = System.Drawing.Color.White;
            this.txtEmail.Location = new System.Drawing.Point(60, 365);
            this.txtEmail.Name = "txtEmail";
            this.txtEmail.Size = new System.Drawing.Size(280, 22);
            this.txtEmail.TabIndex = 9;
            // 
            // lblPassword
            // 
            this.lblPassword.Font = new System.Drawing.Font("Segoe UI", 9F, System.Drawing.FontStyle.Bold);
            this.lblPassword.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(209)))), ((int)(((byte)(213)))), ((int)(((byte)(219)))));
            this.lblPassword.Location = new System.Drawing.Point(60, 400);
            this.lblPassword.Name = "lblPassword";
            this.lblPassword.Size = new System.Drawing.Size(100, 23);
            this.lblPassword.TabIndex = 10;
            this.lblPassword.Text = "CONTRASEÑA";
            // 
            // txtPassword
            // 
            this.txtPassword.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(55)))), ((int)(((byte)(59)))), ((int)(((byte)(65)))));
            this.txtPassword.BorderStyle = System.Windows.Forms.BorderStyle.None;
            this.txtPassword.Font = new System.Drawing.Font("Segoe UI", 12F);
            this.txtPassword.ForeColor = System.Drawing.Color.White;
            this.txtPassword.Location = new System.Drawing.Point(60, 425);
            this.txtPassword.Name = "txtPassword";
            this.txtPassword.Size = new System.Drawing.Size(280, 22);
            this.txtPassword.TabIndex = 11;
            this.txtPassword.UseSystemPasswordChar = true;
            this.txtPassword.TextChanged += new System.EventHandler(this.txtPassword_TextChanged);
            // 
            // progressPasswordStrength
            // 
            this.progressPasswordStrength.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(55)))), ((int)(((byte)(59)))), ((int)(((byte)(65)))));
            this.progressPasswordStrength.Location = new System.Drawing.Point(60, 455);
            this.progressPasswordStrength.Name = "progressPasswordStrength";
            this.progressPasswordStrength.Size = new System.Drawing.Size(280, 6);
            this.progressPasswordStrength.TabIndex = 15;
            this.progressPasswordStrength.Value = 0;
            // 
            // lblStrengthText
            // 
            this.lblStrengthText.AutoSize = true;
            this.lblStrengthText.Font = new System.Drawing.Font("Segoe UI", 8F);
            this.lblStrengthText.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(156)))), ((int)(((byte)(163)))), ((int)(((byte)(175)))));
            this.lblStrengthText.Location = new System.Drawing.Point(60, 465);
            this.lblStrengthText.Name = "lblStrengthText";
            this.lblStrengthText.Size = new System.Drawing.Size(0, 13);
            this.lblStrengthText.TabIndex = 16;
            // 
            // lblConfirmPassword
            // 
            this.lblConfirmPassword.Font = new System.Drawing.Font("Segoe UI", 9F, System.Drawing.FontStyle.Bold);
            this.lblConfirmPassword.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(209)))), ((int)(((byte)(213)))), ((int)(((byte)(219)))));
            this.lblConfirmPassword.Location = new System.Drawing.Point(60, 490);
            this.lblConfirmPassword.Name = "lblConfirmPassword";
            this.lblConfirmPassword.Size = new System.Drawing.Size(280, 23);
            this.lblConfirmPassword.TabIndex = 12;
            this.lblConfirmPassword.Text = "CONFIRMAR CONTRASEÑA";
            // 
            // txtConfirmPassword
            // 
            this.txtConfirmPassword.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(55)))), ((int)(((byte)(59)))), ((int)(((byte)(65)))));
            this.txtConfirmPassword.BorderStyle = System.Windows.Forms.BorderStyle.None;
            this.txtConfirmPassword.Font = new System.Drawing.Font("Segoe UI", 12F);
            this.txtConfirmPassword.ForeColor = System.Drawing.Color.White;
            this.txtConfirmPassword.Location = new System.Drawing.Point(60, 515);
            this.txtConfirmPassword.Name = "txtConfirmPassword";
            this.txtConfirmPassword.Size = new System.Drawing.Size(280, 22);
            this.txtConfirmPassword.TabIndex = 13;
            this.txtConfirmPassword.UseSystemPasswordChar = true;
            // 
            // btnRegister
            // 
            this.btnRegister.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(250)))), ((int)(((byte)(204)))), ((int)(((byte)(21)))));
            this.btnRegister.FlatAppearance.BorderSize = 0;
            this.btnRegister.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnRegister.Font = new System.Drawing.Font("Segoe UI", 11F, System.Drawing.FontStyle.Bold);
            this.btnRegister.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(28)))), ((int)(((byte)(31)))), ((int)(((byte)(34)))));
            this.btnRegister.Location = new System.Drawing.Point(60, 560);
            this.btnRegister.Name = "btnRegister";
            this.btnRegister.Size = new System.Drawing.Size(280, 45);
            this.btnRegister.TabIndex = 14;
            this.btnRegister.Text = "Crear Cuenta";
            this.btnRegister.UseVisualStyleBackColor = false;
            this.btnRegister.Click += new System.EventHandler(this.btnRegister_Click);
            // 
            // lblBackToLogin
            // 
            this.lblBackToLogin.AutoSize = true;
            this.lblBackToLogin.Cursor = System.Windows.Forms.Cursors.Hand;
            this.lblBackToLogin.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(209)))), ((int)(((byte)(213)))), ((int)(((byte)(219)))));
            this.lblBackToLogin.Location = new System.Drawing.Point(120, 620);
            this.lblBackToLogin.Name = "lblBackToLogin";
            this.lblBackToLogin.Size = new System.Drawing.Size(160, 13);
            this.lblBackToLogin.TabIndex = 13;
            this.lblBackToLogin.Text = "¿Ya tienes cuenta? Inicia sesión";
            this.lblBackToLogin.Click += new System.EventHandler(this.lblBackToLogin_Click);
            // 
            // Register
            // 
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(28)))), ((int)(((byte)(31)))), ((int)(((byte)(34)))));
            this.ClientSize = new System.Drawing.Size(900, 760);
            this.Controls.Add(this.panelContainer);
            this.Name = "Register";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Bettr - Registro";
            this.panelContainer.ResumeLayout(false);
            this.panelContainer.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.picLogo)).EndInit();
            this.ResumeLayout(false);
        }

        private System.Windows.Forms.Panel panelContainer;
        private System.Windows.Forms.PictureBox picLogo;
        private System.Windows.Forms.Label lblTitle;
        private System.Windows.Forms.Label lblSubtitle;
        private System.Windows.Forms.Label lblError;
        private System.Windows.Forms.Label lblName;
        private System.Windows.Forms.TextBox txtName;
        private System.Windows.Forms.Label lblUsername;
        private System.Windows.Forms.TextBox txtUsername;
        private System.Windows.Forms.Label lblEmail;
        private System.Windows.Forms.TextBox txtEmail;
        private System.Windows.Forms.Label lblPassword;
        private System.Windows.Forms.TextBox txtPassword;
        private System.Windows.Forms.Label lblConfirmPassword;
        private System.Windows.Forms.TextBox txtConfirmPassword;
        private System.Windows.Forms.Button btnRegister;
        private System.Windows.Forms.Label lblBackToLogin;
        private System.Windows.Forms.Label lblClose;
        private System.Windows.Forms.ProgressBar progressPasswordStrength;
        private System.Windows.Forms.Label lblStrengthText;
    }
}
