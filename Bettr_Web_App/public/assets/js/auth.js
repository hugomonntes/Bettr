

/**
 * Bettr Auth - JavaScript
 * Uses PHP proxy to avoid CORS issues
 */

// PHP Proxy URL
const PROXY_URL = 'api_proxy.php?endpoint=';

// SHA-256 Hash function (matching mobile app)
async function sha256(message) {
    // Encode as UTF-8
    const msgBuffer = new TextEncoder().encode(message);
    // Hash the message
    const hashBuffer = await crypto.subtle.digest('SHA-256', msgBuffer);
    // Convert to hex string
    const hashArray = Array.from(new Uint8Array(hashBuffer));
    const hashHex = hashArray.map(b => b.toString(16).padStart(2, '0')).join('');
    return hashHex;
}

// Show/hide loading
function showLoading() {
    const overlay = document.getElementById('loadingOverlay');
    if (overlay) overlay.classList.add('active');
}

function hideLoading() {
    const overlay = document.getElementById('loadingOverlay');
    if (overlay) overlay.classList.remove('active');
}

// Show toast notification
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

// Show error message
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

// API Functions using PHP proxy
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
        
        // Get response text first
        const responseText = await response.text();
        console.log('API Response Text:', responseText.substring(0, 200));
        
        // Try to parse as JSON
        let data;
        try {
            data = JSON.parse(responseText);
        } catch (e) {
            // If not JSON, use as message
            data = { message: responseText };
        }
        
        console.log('API Response:', response.status, data);
        
        return {
            status: response.status,
            data: data
        };
    } catch (error) {
        console.error('API Error:', error);
        return {
            status: 0,
            data: { message: 'Error de conexión: ' + error.message }
        };
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
        
        const responseText = await response.text();
        
        let responseData;
        try {
            responseData = JSON.parse(responseText);
        } catch (e) {
            responseData = { message: responseText };
        }
        
        console.log('API Response:', response.status, responseData);
        
        return {
            status: response.status,
            data: responseData
        };
    } catch (error) {
        console.error('API Error:', error);
        return {
            status: 0,
            data: { message: 'Error de conexión: ' + error.message }
        };
    }
}

// Login Form Handler - Uses /users/{username}/{password_hash}
const loginForm = document.getElementById('loginForm');
if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value;
        
        if (!username || !password) {
            showError('Por favor completa todos los campos');
            return;
        }
        
        showLoading();
        
        try {
            // Hash password with SHA-256 to match mobile app
            const passwordHash = await sha256(password);
            
            // Login using the correct endpoint: GET /users/{username}/{password_hash}
            const result = await apiGet('users/' + username + '/' + passwordHash);
            
            console.log('Login result:', result);
            
            if (result.status === 200 && result.data && result.data.id) {
                // Get full user data with avatar and description
                const userDataResult = await apiGet('users/' + result.data.id);
                const userData = userDataResult.status === 200 && userDataResult.data ? userDataResult.data : result.data;
                
                // Store user in session
                sessionStorage.setItem('bettr_user', JSON.stringify(userData));
                sessionStorage.setItem('bettr_password', password);
                
                showToast('¡Bienvenido a Bettr!');
                setTimeout(() => {
                    window.location.href = 'dashboard.php';
                }, 1000);
            } else if (result.status === 401) {
                showError('Usuario o contraseña incorrectos');
            } else if (result.status === 0) {
                showError('Error de conexión. Intenta de nuevo.');
            } else {
                showError(result.data.message || 'Usuario o contraseña incorrectos');
            }
        } catch (error) {
            console.error('Login error:', error);
            showError('Error de conexión. Intenta de nuevo.');
        } finally {
            hideLoading();
        }
    });
}

// Register Form Handler
const registerForm = document.getElementById('registerForm');
if (registerForm) {
    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const name = document.getElementById('name').value.trim();
        const username = document.getElementById('username').value.trim();
        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        
        // Validation
        if (!name || !username || !email || !password || !confirmPassword) {
            showError('Por favor completa todos los campos');
            return;
        }
        
        if (password !== confirmPassword) {
            showError('Las contraseñas no coinciden');
            return;
        }
        
        if (password.length < 6) {
            showError('La contraseña debe tener al menos 6 caracteres');
            return;
        }
        
        showLoading();
        
        try {
            // Hash password with SHA-256 to match mobile app
            const passwordHash = await sha256(password);
            
            // Register user - POST /users
            const result = await apiPost('users', {
                name: name,
                username: username,
                email: email,
                password_hash: passwordHash
            });
            
            console.log('Register result:', result);
            
            if (result.status === 201 || result.status === 200) {
                // Auto login after registration to get user data
                const loginResult = await apiGet('users/' + username + '/' + passwordHash);
                
                if (loginResult.status === 200 && loginResult.data && loginResult.data.id) {
                    // Get full user data with avatar and description
                    const userDataResult = await apiGet('users/' + loginResult.data.id);
                    const userData = userDataResult.status === 200 && userDataResult.data ? userDataResult.data : loginResult.data;
                    
                    // Store user in session
                    sessionStorage.setItem('bettr_user', JSON.stringify(userData));
                    sessionStorage.setItem('bettr_password', password);
                    
                    showToast('¡Cuenta creada! Completa tu perfil');
                    setTimeout(() => {
                        window.location.href = 'completar_perfil.php';
                    }, 1500);
                } else {
                    // If auto-login fails, redirect to login
                    showToast('¡Cuenta creada! Inicia sesión');
                    setTimeout(() => {
                        window.location.href = 'login.php';
                    }, 1500);
                }
            } else if (result.status === 500 && result.data && result.data.message) {
                showError('Error: ' + result.data.message);
            } else {
                showError(result.data.message || 'Error al crear la cuenta');
            }
        } catch (error) {
            console.error('Register error:', error);
            showError('Error de conexión. Intenta de nuevo.');
        } finally {
            hideLoading();
        }
    });
}

// Check if user is logged in
function checkAuth() {
    const user = sessionStorage.getItem('bettr_user');
    if (!user && !window.location.href.includes('login.php') && !window.location.href.includes('registro.php')) {
        window.location.href = 'login.php';
    }
    return user ? JSON.parse(user) : null;
}

// Logout function
function logout() {
    sessionStorage.removeItem('bettr_user');
    sessionStorage.removeItem('bettr_password');
    window.location.href = 'login.php';
}

// Get current user
function getCurrentUser() {
    const userStr = sessionStorage.getItem('bettr_user');
    return userStr ? JSON.parse(userStr) : null;
}


