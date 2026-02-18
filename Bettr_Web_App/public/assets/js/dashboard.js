/**
 * Bettr Dashboard - JavaScript
 * Uses PHP proxy to avoid CORS issues
 */

// PHP Proxy URL
const PROXY_URL = 'api_proxy.php?endpoint=';

// Global state
let currentUser = null;
let currentPage = 'feed';

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    loadUserFromSession();
    setupNavigation();
    loadFeed();
});

// Load user from session
function loadUserFromSession() {
    const userStr = sessionStorage.getItem('bettr_user');
    if (!userStr) {
        window.location.href = 'login.php';
        return;
    }
    
    currentUser = JSON.parse(userStr);
    updateUserUI();
}

// Update user UI
function updateUserUI() {
    if (!currentUser) return;
    
    const firstLetter = currentUser.name ? currentUser.name.charAt(0).toUpperCase() : 'U';
    const avatarEl = document.getElementById('userAvatar');
    if (avatarEl) avatarEl.textContent = firstLetter;
    
    const nameEl = document.getElementById('userName');
    if (nameEl) nameEl.textContent = currentUser.name || 'Usuario';
    
    const usernameEl = document.getElementById('userUsername');
    if (usernameEl) usernameEl.textContent = '@' + (currentUser.username || 'user');
}

// Setup navigation
function setupNavigation() {
    const navItems = document.querySelectorAll('.nav-item[data-page]');
    navItems.forEach(item => {
        item.addEventListener('click', (e) => {
            e.preventDefault();
            const page = item.dataset.page;
            navigateTo(page);
        });
    });
}

// Navigate to page
function navigateTo(page) {
    currentPage = page;
    
    // Update active nav
    document.querySelectorAll('.nav-item').forEach(item => {
        item.classList.remove('active');
        if (item.dataset.page === page) {
            item.classList.add('active');
        }
    });
    
    // Load page content
    switch(page) {
        case 'feed':
            loadFeed();
            break;
        case 'habits':
            loadMyHabits();
            break;
        case 'discover':
            loadDiscover();
            break;
        case 'profile':
            loadProfile();
            break;
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

async function apiDelete(endpoint) {
    let url = PROXY_URL + encodeURIComponent(endpoint);
    
    console.log('API DELETE:', url);
    
    try {
        // Use POST with custom header to override method, as DELETE with body is not well supported
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-HTTP-Method-Override': 'DELETE'
            }
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

// Load Feed
async function loadFeed() {
    const pageTitle = document.getElementById('pageTitle');
    if (pageTitle) pageTitle.textContent = 'Inicio';
    
    const content = document.getElementById('mainContent');
    if (!content) return;
    
    content.innerHTML = '<div class="loading-spinner" style="margin:40px auto;"></div>';
    
    try {
        const result = await apiGet('habits/feed/' + currentUser.id);
        
        console.log('Feed result:', result);
        
        if (result.status === 200 && result.data) {
            renderFeed(result.data);
        } else {
            content.innerHTML = '<div class="empty-state"><p>No hay publicaciones aún</p></div>';
        }
    } catch (error) {
        console.error('Feed error:', error);
        content.innerHTML = '<div class="empty-state"><p>Error al cargar el feed</p></div>';
    }
}

// Render Feed
function renderFeed(habits) {
    const content = document.getElementById('mainContent');
    if (!content) return;
    
    if (!habits || habits.length === 0) {
        content.innerHTML = `
            <div class="empty-state">
                <div class="empty-state-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" width="80" height="80">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
                    </svg>
                </div>
                <h3 class="empty-state-title">No hay publicaciones</h3>
                <p class="empty-state-text">Sigue a otros usuarios para ver sus hábitos</p>
            </div>
        `;
        return;
    }
    
    let html = '';
    habits.forEach(habit => {
        const avatarLetter = habit.username ? habit.username.charAt(0).toUpperCase() : 'U';
        const timeAgo = getTimeAgo(habit.created_at);
        
        html += `
            <article class="card post">
                <div class="card-header">
                    <div class="card-avatar">${avatarLetter}</div>
                    <div class="card-user-info">
                        <div class="card-username">@${habit.username || 'usuario'}</div>
                        <div class="card-time">${timeAgo}</div>
                    </div>
                    <span class="habit-type-badge">${habit.habit_type || 'Hábito'}</span>
                </div>
                
                <div class="post-image">
                    ${habit.image_url ? `<img src="${habit.image_url}" alt="Habit image">` : ''}
                </div>
                
                <div class="post-actions">
                    <span class="post-action like-btn" data-habit-id="${habit.id}" onclick="toggleLike(${habit.id}, this)">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                        </svg>
                        <span class="like-count">${habit.likes_count || 0}</span>
                    </span>
                    <span class="post-action">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
                        </svg>
                        Comentar
                    </span>
                </div>
                
                <p class="post-text">${habit.description || 'Nuevo hábito completado!'}</p>
            </article>
        `;
    });
    
    content.innerHTML = html;
}

// Load My Habits
async function loadMyHabits() {
    const pageTitle = document.getElementById('pageTitle');
    if (pageTitle) pageTitle.textContent = 'Mis Hábitos';
    
    const content = document.getElementById('mainContent');
    if (!content) return;
    
    content.innerHTML = '<div class="loading-spinner" style="margin:40px auto;"></div>';
    
    try {
        const result = await apiGet('habits/user/' + currentUser.id);
        
        if (result.status === 200 && result.data) {
            renderFeed(result.data);
        } else {
            content.innerHTML = '<div class="empty-state"><p>No tienes hábitos aún</p></div>';
        }
    } catch (error) {
        content.innerHTML = '<div class="empty-state"><p>Error al cargar hábitos</p></div>';
    }
}

// Load Discover (Search Users)
async function loadDiscover() {
    const pageTitle = document.getElementById('pageTitle');
    if (pageTitle) pageTitle.textContent = 'Descubrir';
    
    const content = document.getElementById('mainContent');
    if (!content) return;
    
    content.innerHTML = `
        <div class="search-bar">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
            <input type="text" id="searchInput" placeholder="Buscar usuarios..." onkeyup="searchUsers(this.value)">
        </div>
        <div id="usersList">
            <div class="loading-spinner" style="margin:40px auto;"></div>
        </div>
    `;
    
    // Load all users initially
    searchUsers('');
}

// Search Users
async function searchUsers(query) {
    const usersList = document.getElementById('usersList');
    if (!usersList) return;
    
    usersList.innerHTML = '<div class="loading-spinner" style="margin:40px auto;"></div>';
    
    try {
        const result = await apiGet('users', { search: query || '' });
        
        console.log('Users result:', result);
        
        if (result.status === 200 && result.data) {
            // Filter out current user
            const users = result.data.filter(u => u.id !== currentUser.id);
            
            // Get following status for each user
            for (let user of users) {
                const followStatus = await apiGet('users/isfollowing/' + currentUser.id + '/' + user.id);
                user.isFollowing = followStatus.data && followStatus.data.following === true;
            }
            
            renderUsers(users);
        } else {
            usersList.innerHTML = '<div class="empty-state"><p>No se encontraron usuarios</p></div>';
        }
    } catch (error) {
        usersList.innerHTML = '<div class="empty-state"><p>Error al buscar usuarios</p></div>';
    }
}

// Render Users
function renderUsers(users) {
    const usersList = document.getElementById('usersList');
    if (!usersList) return;
    
    if (!users || users.length === 0) {
        usersList.innerHTML = '<div class="empty-state"><p>No se encontraron usuarios</p></div>';
        return;
    }
    
    let html = '';
    users.forEach(user => {
        const avatarLetter = user.name ? user.name.charAt(0).toUpperCase() : 'U';
        const followBtnText = user.isFollowing ? 'Siguiendo' : 'Seguir';
        const followBtnClass = user.isFollowing ? 'btn btn-secondary following' : 'btn btn-primary';
        
        html += `
            <div class="user-item">
                <div class="user-item-avatar">${avatarLetter}</div>
                <div class="user-item-info">
                    <div class="user-item-name">${user.name || 'Usuario'}</div>
                    <div class="user-item-username">@${user.username || 'user'}</div>
                </div>
                <button class="${followBtnClass}" onclick="toggleFollow(${user.id}, this)">${followBtnText}</button>
            </div>
        `;
    });
    
    usersList.innerHTML = html;
}

// Toggle Follow/Unfollow
async function toggleFollow(userId, button) {
    const isFollowing = button.classList.contains('following');
    
    if (isFollowing) {
        // Unfollow
        const result = await apiDelete('users/unfollow/' + currentUser.id + '/' + userId);
        
        if (result.status === 200 || result.status === 201) {
            button.textContent = 'Seguir';
            button.classList.remove('btn-secondary', 'following');
            button.classList.add('btn-primary');
            showToast('Has dejado de seguir a este usuario');
        } else {
            showToast('Error al dejar de seguir', 'error');
        }
    } else {
        // Follow
        const result = await apiPost('users/follow/' + currentUser.id + '/' + userId, {});
        
        if (result.status === 200 || result.status === 201) {
            button.textContent = 'Siguiendo';
            button.classList.remove('btn-primary');
            button.classList.add('btn-secondary', 'following');
            showToast('¡Ahora sigues a este usuario!');
        } else {
            showToast('Error al seguir usuario', 'error');
        }
    }
}

// Load Profile
async function loadProfile() {
    const pageTitle = document.getElementById('pageTitle');
    if (pageTitle) pageTitle.textContent = 'Mi Perfil';
    
    const content = document.getElementById('mainContent');
    if (!content) return;
    
    content.innerHTML = '<div class="loading-spinner" style="margin:40px auto;"></div>';
    
    try {
        // Get user stats
        const statsResult = await apiGet('users/' + currentUser.id + '/stats');
        
        let stats = { followers: 0, following: 0, habits: 0 };
        if (statsResult.status === 200 && statsResult.data) {
            stats = statsResult.data;
        }
        
        const avatarLetter = currentUser.name ? currentUser.name.charAt(0).toUpperCase() : 'U';
        
        content.innerHTML = `
            <div class="card">
                <div class="profile-header">
                    <div class="profile-avatar-large">${avatarLetter}</div>
                    <h2 class="profile-name">${currentUser.name || 'Usuario'}</h2>
                    <p class="profile-username">@${currentUser.username || 'user'}</p>
                    <p class="profile-username">${currentUser.email || ''}</p>
                    <div class="profile-stats">
                        <div class="profile-stat" onclick="showFollowers()">
                            <div class="profile-stat-value">${stats.followers || 0}</div>
                            <div class="profile-stat-label">Seguidores</div>
                        </div>
                        <div class="profile-stat" onclick="showFollowing()">
                            <div class="profile-stat-value">${stats.following || 0}</div>
                            <div class="profile-stat-label">Siguiendo</div>
                        </div>
                        <div class="profile-stat">
                            <div class="profile-stat-value">${stats.habits || 0}</div>
                            <div class="profile-stat-label">Hábitos</div>
                        </div>
                    </div>
                </div>
            </div>
            
            <button class="btn btn-primary btn-full" onclick="logout()">Cerrar Sesión</button>
        `;
    } catch (error) {
        console.error('Profile error:', error);
        content.innerHTML = '<div class="empty-state"><p>Error al cargar el perfil</p></div>';
    }
}

// Show Followers
async function showFollowers() {
    const content = document.getElementById('mainContent');
    if (!content) return;
    
    content.innerHTML = '<div class="loading-spinner" style="margin:40px auto;"></div>';
    
    try {
        const result = await apiGet('users/' + currentUser.id + '/followers');
        
        if (result.status === 200 && result.data) {
            const users = result.data;
            
            if (users.length === 0) {
                content.innerHTML = `
                    <div class="empty-state">
                        <h3 class="empty-state-title">Sin seguidores aún</h3>
                        <p class="empty-state-text">¡Comparte tu perfil para conseguir seguidores!</p>
                        <button class="btn btn-primary" onclick="loadProfile()">Volver al perfil</button>
                    </div>
                `;
                return;
            }
            
            let html = '<button class="btn btn-secondary" style="margin-bottom: 16px;" onclick="loadProfile()">← Volver</button>';
            
            users.forEach(user => {
                const avatarLetter = user.name ? user.name.charAt(0).toUpperCase() : 'U';
                html += `
                    <div class="user-item">
                        <div class="user-item-avatar">${avatarLetter}</div>
                        <div class="user-item-info">
                            <div class="user-item-name">${user.name || 'Usuario'}</div>
                            <div class="user-item-username">@${user.username || 'user'}</div>
                        </div>
                    </div>
                `;
            });
            
            content.innerHTML = html;
        } else {
            content.innerHTML = '<div class="empty-state"><p>Error al cargar seguidores</p></div>';
        }
    } catch (error) {
        content.innerHTML = '<div class="empty-state"><p>Error al cargar seguidores</p></div>';
    }
}

// Show Following
async function showFollowing() {
    const content = document.getElementById('mainContent');
    if (!content) return;
    
    content.innerHTML = '<div class="loading-spinner" style="margin:40px auto;"></div>';
    
    try {
        const result = await apiGet('users/' + currentUser.id + '/following');
        
        if (result.status === 200 && result.data) {
            const users = result.data;
            
            if (users.length === 0) {
                content.innerHTML = `
                    <div class="empty-state">
                        <h3 class="empty-state-title">No sigues a nadie</h3>
                        <p class="empty-state-text">¡Descubre nuevos usuarios en la pestaña Descubrir!</p>
                        <button class="btn btn-primary" onclick="loadProfile()">Volver al perfil</button>
                    </div>
                `;
                return;
            }
            
            let html = '<button class="btn btn-secondary" style="margin-bottom: 16px;" onclick="loadProfile()">← Volver</button>';
            
            users.forEach(user => {
                const avatarLetter = user.name ? user.name.charAt(0).toUpperCase() : 'U';
                html += `
                    <div class="user-item">
                        <div class="user-item-avatar">${avatarLetter}</div>
                        <div class="user-item-info">
                            <div class="user-item-name">${user.name || 'Usuario'}</div>
                            <div class="user-item-username">@${user.username || 'user'}</div>
                        </div>
                    </div>
                `;
            });
            
            content.innerHTML = html;
        } else {
            content.innerHTML = '<div class="empty-state"><p>Error al cargar siguiendo</p></div>';
        }
    } catch (error) {
        content.innerHTML = '<div class="empty-state"><p>Error al cargar siguiendo</p></div>';
    }
}

// Toggle Like
async function toggleLike(habitId, button) {
    const isLiked = button.classList.contains('liked');
    
    if (isLiked) {
        // Unlike
        const result = await apiDelete('habits/' + habitId + '/like/' + currentUser.id);
        
        if (result.status === 200) {
            button.classList.remove('liked');
            const likeCount = button.querySelector('.like-count');
            if (likeCount) {
                likeCount.textContent = Math.max(0, parseInt(likeCount.textContent) - 1);
            }
        }
    } else {
        // Like
        const result = await apiPost('habits/' + habitId + '/like/' + currentUser.id, {});
        
        if (result.status === 200 || result.status === 201) {
            button.classList.add('liked');
            const likeCount = button.querySelector('.like-count');
            if (likeCount) {
                likeCount.textContent = parseInt(likeCount.textContent) + 1;
            }
        }
    }
}

// Modal Functions
function openHabitModal() {
    const modal = document.getElementById('habitModal');
    if (modal) modal.classList.add('active');
}

function closeHabitModal() {
    const modal = document.getElementById('habitModal');
    if (modal) {
        modal.classList.remove('active');
        const form = document.getElementById('habitForm');
        if (form) form.reset();
    }
}

// Submit Habit
const habitForm = document.getElementById('habitForm');
if (habitForm) {
    habitForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const habitType = document.getElementById('habitType')?.value;
        const description = document.getElementById('habitDescription')?.value;
        
        showLoading();
        
        try {
            const result = await apiPost('habits', {
                user_id: currentUser.id,
                description: description,
                image_url: '',
                habit_type: habitType
            });
            
            console.log('Habit result:', result);
            
            if (result.status === 200 || result.status === 201) {
                showToast('¡Hábito compartido!');
                closeHabitModal();
                loadFeed();
            } else {
                showToast('Error al compartir hábito', 'error');
            }
        } catch (error) {
            showToast('Error de conexión', 'error');
        } finally {
            hideLoading();
        }
    });
}

// Utility Functions
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

function logout() {
    sessionStorage.removeItem('bettr_user');
    sessionStorage.removeItem('bettr_password');
    window.location.href = 'login.php';
}

function getTimeAgo(dateString) {
    if (!dateString) return 'Ahora';
    
    const date = new Date(dateString);
    const now = new Date();
    const diff = Math.floor((now - date) / 1000);
    
    if (diff < 60) return 'Ahora';
    if (diff < 3600) return Math.floor(diff / 60) + 'm';
    if (diff < 86400) return Math.floor(diff / 3600) + 'h';
    if (diff < 604800) return Math.floor(diff / 86400) + 'd';
    return Math.floor(diff / 604800) + 'sem';
}

// Close modal on outside click
const habitModal = document.getElementById('habitModal');
if (habitModal) {
    habitModal.addEventListener('click', (e) => {
        if (e.target.classList.contains('modal-overlay')) {
            closeHabitModal();
        }
    });
}

