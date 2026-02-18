/**
 * Bettr Main JavaScript
 * Global utility functions
 */

// API Configuration

const API_BASE_URL = 'https://bettr-g5yv.onrender.com/rest';

// Check if user is logged in
function requireAuth() {
    const user = sessionStorage.getItem('bettr_user');
    if (!user) {
        window.location.href = 'login.php';
        return false;
    }
    return true;
}

// Get current user
function getCurrentUser() {
    const userStr = sessionStorage.getItem('bettr_user');
    return userStr ? JSON.parse(userStr) : null;
}

// Get user initials
function getUserInitials(name) {
    if (!name) return 'U';
    return name.charAt(0).toUpperCase();
}

// Format date
function formatDate(dateString) {
    if (!dateString) return 'Ahora';
    
    const date = new Date(dateString);
    const now = new Date();
    const diff = Math.floor((now - date) / 1000);
    
    if (diff < 60) return 'Ahora';
    if (diff < 3600) return Math.floor(diff / 60) + 'm';
    if (diff < 86400) return Math.floor(diff / 3600) + 'h';
    if (diff < 604800) return Math.floor(diff / 86400) + 'd';
    
    return date.toLocaleDateString('es-ES');
}

// Debounce function
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Local storage helpers
const storage = {
    set: (key, value) => {
        try {
            localStorage.setItem(key, JSON.stringify(value));
        } catch (e) {
            console.error('Error saving to localStorage:', e);
        }
    },
    get: (key) => {
        try {
            const item = localStorage.getItem(key);
            return item ? JSON.parse(item) : null;
        } catch (e) {
            console.error('Error reading from localStorage:', e);
            return null;
        }
    },
    remove: (key) => {
        try {
            localStorage.removeItem(key);
        } catch (e) {
            console.error('Error removing from localStorage:', e);
        }
    }
};

// Initialize app
document.addEventListener('DOMContentLoaded', () => {
    // Add any global initialization here
    console.log('Bettr App initialized');
});

