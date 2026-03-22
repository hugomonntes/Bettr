namespace Bettr_Desktop_App
{
    partial class CompleteProfile
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
            this.lblClose = new System.Windows.Forms.Label();
            this.picLogo = new System.Windows.Forms.PictureBox();
            this.lblTitle = new System.Windows.Forms.Label();
            this.lblSubtitle = new System.Windows.Forms.Label();
            this.picAvatar = new System.Windows.Forms.PictureBox();
            this.lblAvatarLetter = new System.Windows.Forms.Label();
            this.lblChangePhoto = new System.Windows.Forms.Label();
            this.lblDescription = new System.Windows.Forms.Label();
            this.txtDescription = new System.Windows.Forms.TextBox();
            this.btnSave = new System.Windows.Forms.Button();
            this.lblSkip = new System.Windows.Forms.Label();
            this.panelContainer.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.picLogo)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.picAvatar)).BeginInit();
            this.SuspendLayout();
            // 
            // panelContainer
            // 
            this.panelContainer.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(42)))), ((int)(((byte)(46)))), ((int)(((byte)(51)))));
            this.panelContainer.Controls.Add(this.lblClose);
            this.panelContainer.Controls.Add(this.picLogo);
            this.panelContainer.Controls.Add(this.lblTitle);
            this.panelContainer.Controls.Add(this.lblSubtitle);
            this.panelContainer.Controls.Add(this.picAvatar);
            this.panelContainer.Controls.Add(this.lblAvatarLetter);
            this.panelContainer.Controls.Add(this.lblChangePhoto);
            this.panelContainer.Controls.Add(this.lblDescription);
            this.panelContainer.Controls.Add(this.txtDescription);
            this.panelContainer.Controls.Add(this.btnSave);
            this.panelContainer.Controls.Add(this.lblSkip);
            this.panelContainer.Location = new System.Drawing.Point(250, 60);
            this.panelContainer.Name = "panelContainer";
            this.panelContainer.Size = new System.Drawing.Size(400, 580);
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
            this.lblClose.TabIndex = 0;
            this.lblClose.Text = "X";
            this.lblClose.Click += new System.EventHandler(this.lblClose_Click);
            // 
            // picLogo
            // 
            this.picLogo.Location = new System.Drawing.Point(140, 30);
            this.picLogo.Name = "picLogo";
            this.picLogo.Size = new System.Drawing.Size(120, 100);
            this.picLogo.SizeMode = System.Windows.Forms.PictureBoxSizeMode.Zoom;
            this.picLogo.TabIndex = 1;
            this.picLogo.TabStop = false;
            // 
            // lblTitle
            // 
            this.lblTitle.AutoSize = true;
            this.lblTitle.Font = new System.Drawing.Font("Segoe UI", 20F, System.Drawing.FontStyle.Bold);
            this.lblTitle.ForeColor = System.Drawing.Color.White;
            this.lblTitle.Location = new System.Drawing.Point(70, 145);
            this.lblTitle.Name = "lblTitle";
            this.lblTitle.Size = new System.Drawing.Size(260, 37);
            this.lblTitle.TabIndex = 2;
            this.lblTitle.Text = "¡Bienvenido a Bettr!";
            // 
            // lblSubtitle
            // 
            this.lblSubtitle.AutoSize = true;
            this.lblSubtitle.Font = new System.Drawing.Font("Segoe UI", 10F);
            this.lblSubtitle.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(209)))), ((int)(((byte)(213)))), ((int)(((byte)(219)))));
            this.lblSubtitle.Location = new System.Drawing.Point(55, 185);
            this.lblSubtitle.Name = "lblSubtitle";
            this.lblSubtitle.Size = new System.Drawing.Size(290, 19);
            this.lblSubtitle.TabIndex = 3;
            this.lblSubtitle.Text = "Completa tu perfil para personalizar tu experiencia";
            this.lblSubtitle.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // picAvatar
            // 
            this.picAvatar.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(102)))), ((int)(((byte)(126)))), ((int)(((byte)(234)))));
            this.picAvatar.Cursor = System.Windows.Forms.Cursors.Hand;
            this.picAvatar.Location = new System.Drawing.Point(140, 220);
            this.picAvatar.Name = "picAvatar";
            this.picAvatar.Size = new System.Drawing.Size(120, 120);
            this.picAvatar.SizeMode = System.Windows.Forms.PictureBoxSizeMode.Zoom;
            this.picAvatar.TabIndex = 4;
            this.picAvatar.TabStop = false;
            this.picAvatar.Click += new System.EventHandler(this.picAvatar_Click);
            // 
            // lblAvatarLetter
            // 
            this.lblAvatarLetter.AutoSize = false;
            this.lblAvatarLetter.Font = new System.Drawing.Font("Segoe UI", 48F, System.Drawing.FontStyle.Bold);
            this.lblAvatarLetter.ForeColor = System.Drawing.Color.White;
            this.lblAvatarLetter.Location = new System.Drawing.Point(140, 220);
            this.lblAvatarLetter.Name = "lblAvatarLetter";
            this.lblAvatarLetter.Size = new System.Drawing.Size(120, 120);
            this.lblAvatarLetter.TabIndex = 5;
            this.lblAvatarLetter.Text = "U";
            this.lblAvatarLetter.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // lblChangePhoto
            // 
            this.lblChangePhoto.AutoSize = true;
            this.lblChangePhoto.Cursor = System.Windows.Forms.Cursors.Hand;
            this.lblChangePhoto.Font = new System.Drawing.Font("Segoe UI", 9F);
            this.lblChangePhoto.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(102)))), ((int)(((byte)(126)))), ((int)(((byte)(234)))));
            this.lblChangePhoto.Location = new System.Drawing.Point(135, 350);
            this.lblChangePhoto.Name = "lblChangePhoto";
            this.lblChangePhoto.Size = new System.Drawing.Size(130, 15);
            this.lblChangePhoto.TabIndex = 6;
            this.lblChangePhoto.Text = "Cambiar foto de perfil";
            this.lblChangePhoto.Click += new System.EventHandler(this.picAvatar_Click);
            // 
            // lblDescription
            // 
            this.lblDescription.AutoSize = true;
            this.lblDescription.Font = new System.Drawing.Font("Segoe UI", 9F, System.Drawing.FontStyle.Bold);
            this.lblDescription.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(209)))), ((int)(((byte)(213)))), ((int)(((byte)(219)))));
            this.lblDescription.Location = new System.Drawing.Point(60, 390);
            this.lblDescription.Name = "lblDescription";
            this.lblDescription.Size = new System.Drawing.Size(100, 23);
            this.lblDescription.TabIndex = 7;
            this.lblDescription.Text = "DESCRIPCIÓN";
            // 
            // txtDescription
            // 
            this.txtDescription.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(55)))), ((int)(((byte)(59)))), ((int)(((byte)(65)))));
            this.txtDescription.BorderStyle = System.Windows.Forms.BorderStyle.None;
            this.txtDescription.Font = new System.Drawing.Font("Segoe UI", 11F);
            this.txtDescription.ForeColor = System.Drawing.Color.White;
            this.txtDescription.Location = new System.Drawing.Point(60, 415);
            this.txtDescription.Multiline = true;
            this.txtDescription.Name = "txtDescription";
            this.txtDescription.Size = new System.Drawing.Size(280, 80);
            this.txtDescription.TabIndex = 8;
            this.txtDescription.Text = "Cuéntanos sobre ti... ¿Cuáles son tus objetivos?";
            this.txtDescription.TextAlign = System.Windows.Forms.HorizontalAlignment.Left;
            // 
            // btnSave
            // 
            this.btnSave.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(250)))), ((int)(((byte)(204)))), ((int)(((byte)(21)))));
            this.btnSave.FlatAppearance.BorderSize = 0;
            this.btnSave.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnSave.Font = new System.Drawing.Font("Segoe UI", 11F, System.Drawing.FontStyle.Bold);
            this.btnSave.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(28)))), ((int)(((byte)(31)))), ((int)(((byte)(34)))));
            this.btnSave.Location = new System.Drawing.Point(60, 510);
            this.btnSave.Name = "btnSave";
            this.btnSave.Size = new System.Drawing.Size(280, 45);
            this.btnSave.TabIndex = 9;
            this.btnSave.Text = "Guardar y Continuar";
            this.btnSave.UseVisualStyleBackColor = false;
            this.btnSave.Click += new System.EventHandler(this.btnSave_Click);
            // 
            // lblSkip
            // 
            this.lblSkip.AutoSize = true;
            this.lblSkip.Cursor = System.Windows.Forms.Cursors.Hand;
            this.lblSkip.Font = new System.Drawing.Font("Segoe UI", 9F);
            this.lblSkip.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(156)))), ((int)(((byte)(163)))), ((int)(((byte)(175)))));
            this.lblSkip.Location = new System.Drawing.Point(155, 560);
            this.lblSkip.Name = "lblSkip";
            this.lblSkip.Size = new System.Drawing.Size(90, 15);
            this.lblSkip.TabIndex = 10;
            this.lblSkip.Text = "Omitir por ahora";
            this.lblSkip.Click += new System.EventHandler(this.lblSkip_Click);
            // 
            // CompleteProfile
            // 
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(28)))), ((int)(((byte)(31)))), ((int)(((byte)(34)))));
            this.ClientSize = new System.Drawing.Size(900, 700);
            this.Controls.Add(this.panelContainer);
            this.Name = "CompleteProfile";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Bettr - Completa tu Perfil";
            this.panelContainer.ResumeLayout(false);
            this.panelContainer.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.picLogo)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.picAvatar)).EndInit();
            this.ResumeLayout(false);
        }

        private System.Windows.Forms.Panel panelContainer;
        private System.Windows.Forms.Label lblClose;
        private System.Windows.Forms.PictureBox picLogo;
        private System.Windows.Forms.Label lblTitle;
        private System.Windows.Forms.Label lblSubtitle;
        private System.Windows.Forms.PictureBox picAvatar;
        private System.Windows.Forms.Label lblAvatarLetter;
        private System.Windows.Forms.Label lblChangePhoto;
        private System.Windows.Forms.Label lblDescription;
        private System.Windows.Forms.TextBox txtDescription;
        private System.Windows.Forms.Button btnSave;
        private System.Windows.Forms.Label lblSkip;
    }
}
