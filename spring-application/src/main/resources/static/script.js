import CharactersTab from './js/characters.js';
import MatchesTab from './js/matches.js';
import LeaderboardTab from './js/leaderboard.js';

// Common configurations
export const CHARACTER_CLASSES = {
    WARRIOR: {
        name: 'Warrior',
        properties: {
            health: { type: 'number', min: 1, max: 200, default: 110 },
            attackPower: { type: 'number', min: 1, max: 100, default: 40 },
            stamina: { type: 'number', min: 1, max: 100, default: 20 },
            defensePower: { type: 'number', min: 1, max: 100, default: 30 }
        }
    },
    SORCERER: {
        name: 'Sorcerer',
        properties: {
            health: { type: 'number', min: 1, max: 200, default: 100 },
            attackPower: { type: 'number', min: 1, max: 100, default: 40 },
            mana: { type: 'number', min: 0, max: 100, default: 30 },
            healingPower: { type: 'number', min: 0, max: 100, default: 30 }
        }
    }
};

export const COMMON_DISPLAY_PROPERTIES = ['health', 'attackPower'];
export const CLASS_SPECIFIC_PROPERTIES = {
    WARRIOR: ['stamina', 'defensePower'],
    SORCERER: ['mana', 'healingPower']
};

// Utility functions
export function showToast(message, isError = false) {
    const toast = document.createElement('div');
    toast.className = `toast ${isError ? 'toast-error' : 'toast-success'}`;
    toast.textContent = message;
    document.body.appendChild(toast);

    setTimeout(() => {
        toast.remove();
    }, 3000);
}

export function checkAuth() {
    const auth = window.sessionStorage.getItem('auth');
    if (!auth) {
        window.location.href = '/login.html';
        return false;
    }
    return true;
}

export async function fetchWithAuth(url, options = {}) {
    const auth = window.sessionStorage.getItem('auth');
    if (!auth) {
        window.location.href = '/login.html';
        throw new Error('Not authenticated');
    }

    const defaultOptions = {
        headers: {
            'Authorization': 'Basic ' + auth,
            'Content-Type': 'application/json',
        },
    };

    try {
        const response = await fetch(url, {
            ...defaultOptions,
            ...options,
            headers: { ...defaultOptions.headers, ...options.headers }
        });

        // Handle 401 Unauthorized specifically
        if (response.status === 401) {
            window.sessionStorage.removeItem('auth');
            window.location.href = '/login.html';
            throw new Error('Session expired');
        }

        // Try to parse the response as JSON if it's not already parsed
        let data;
        if (response instanceof Response) {
            data = await response.json();
        } else {
            data = response; // Response is already parsed JSON
        }

        // If response is not ok, throw error with server message
        if (!response.ok) {
            // Handle ErrorResponse format
            if (data.error) {
                throw new Error(data.error);
            }
            // Fallback error message
            throw new Error(`Error: ${response.status} - ${response.statusText}`);
        }

        return data;
    } catch (error) {
        // If error is already handled (has message), just rethrow
        if (error.message) {
            throw error;
        }
        // Handle unexpected errors
        console.error('Fetch error:', error);
        throw new Error('An unexpected error occurred');
    }
}

// Initialize tabs when DOM is loaded
document.addEventListener('DOMContentLoaded', async () => {
    if (!checkAuth()) return;

    try {
        // Initialize all tabs
        const charactersTab = new CharactersTab();
        const matchesTab = new MatchesTab();
        const leaderboardTab = new LeaderboardTab();

        // Show characters tab by default
        const charactersTabEl = document.getElementById('characters-tab');
        const tab = new bootstrap.Tab(charactersTabEl);
        tab.show();
    } catch (error) {
        console.error('Error initializing application:', error);
        showToast('Failed to initialize application', true);
    }
});

function handleLogout() {
    // Clear the token
    localStorage.removeItem('token');
    // Redirect to login page
    window.location.href = '/login.html';
}
