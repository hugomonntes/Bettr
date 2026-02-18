const PROXY_URL = 'api_proxy.php?endpoint=';

let currentUser = null;
let avatarBase64 = '';

document.addEventListener('DOMContentLoaded', () => {
    loadUserFromSession();
    setupAvatarUpload();
});

function loadUserFromSession() {
    const userStr = sessionStorage.getItem('bettr_user');
    if (!userStr) {
        window.location.href = 'login.php';
        return;
    }
    
    currentUser = JSON.parse(userStr);
    updateAvatarPreview();
}

function updateAvatarPreview() {
    if (!currentUser) return;
    
    const firstLetter = currentUser.name ? currentUser.name.charAt(0).toUpperCase() : 'U';
    const avatarLetter = document.getElementById('avatarLetter');
    if (avatarLetter) {
        avatarLetter.textContent = firstLetter;
    }
}

function setupAvatarUpload() {
    const avatarInput = document.getElementById('avatarInput');
    if (avatarInput) {
        avatarInput.addEventListener('change', handleAvatarChange);
    }
}

function handleAvatarChange(e) {
    const file = e.target.files[0];
    if (!file) return;
    
    if (file.size > 5 * 1024 * 1024) {
        showError('La imagen debe ser menor de 5MB');
        return;
    }
    
    if (!file.type.match(/image\/(jpeg|jpg|png|gif)/)) {
        showError('Solo se permiten imágenes JPG, PNG o GIF');
        return;
    }
    
    const reader = new FileReader();
    reader.onload = function(event) {
        avatarBase64 = event.target.result;
        
        const avatarPreview = document.getElementById('avatarPreview');
        avatarPreview.innerHTML = `<img src="${avatarBase64}" alt="Avatar">`;
    };
    reader.onerror = function() {
        showError('Error al leer la imagen');
    };
    reader.readAsDataURL(file);
}

async function apiGet(endpoint, params = {}) {
    let url = PROXY_URL + encodeURIComponent(endpoint);
    
    if (Object.keys(params).length > 0) {
        const queryString = new URLSearchParams(params).toString();
        url += '&' + queryString;
    }
    
    console.log('API GET:', url);
    
    try {
        const response = await fetch(url, {
            method: 'GET'
        });
        
        const data = await response.json();
        console.log('API Response:', response.status, data);
        
        return {
            status: response.status,
            data: data
        };
    } catch (error) {
        console.error('API Error:', error);
        return { status: 0, data: { message: 'Error de conexión: ' + error.message } };
    }
}

async function apiPost(endpoint, data) {
    let url = PROXY_URL + encodeURIComponent(endpoint);
    
    console.log('API POST:', url, data);
    
    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        
        const responseData = await response.json();
        console.log('API Response:', response.status, responseData);
        
        return {
            status: response.status,
            data: responseData
        };
    } catch (error) {
        console.error('API Error:', error);
        return { status: 0, data: { message: 'Error de conexión: ' + error.message } };
    }
}

const profileForm = document.getElementById('profileForm');
if (profileForm) {
    profileForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const description = document.getElementById('description')?.value.trim() || '';
        
        if (!currentUser) {
            showError('Sesión no válida');
            return;
        }
        
        showLoading();
        
        try {
            const result = await apiPost('users/' + currentUser.id + '/profile', {
                description: description,
                avatar: avatarBase64
            });
            
            console.log('Profile update result:', result);
            
            if (result.status === 200 || result.status === 201) {
                currentUser.description = description;
                currentUser.avatar = avatarBase64;
                sessionStorage.setItem('bettr_user', JSON.stringify(currentUser));
                
                showToast('¡Perfil actualizado!');
                setTimeout(() => {
                    window.location.href = 'dashboard.php';
                }, 1000);
            } else {
                showError(result.data.message || 'Error al guardar el perfil');
            }
        } catch (error) {
            console.error('Profile update error:', error);
            showError('Error de conexión. Intenta de nuevo.');
        } finally {
            hideLoading();
        }
    });
}

function showLoading() {
    const overlay = document.getElementById('loadingOverlay');
    if (overlay) overlay.classList.add('active');
}

function hideLoading() {
    const overlay = document.getElementById('loadingOverlay');
    if (overlay) overlay.classList.remove('active');
}

function showToast(message, type = 'success') {
    const toast = document.getElementById('toast');
    if (toast) {
        toast.textContent = message;
        toast.className = `toast ${type} show`;
        
        setTimeout(() => {
            toast.classList.remove('show');
        }, 3000);
    }
}

function showError(message) {
    const errorDiv = document.getElementById('errorMessage');
    if (errorDiv) {
        errorDiv.textContent = message;
        errorDiv.classList.add('show');
        
        setTimeout(() => {
            errorDiv.classList.remove('show');
        }, 5000);
    }
}

