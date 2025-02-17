document.getElementById('loginForm').addEventListener('submit', async (event) => {
    event.preventDefault();
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const authToken = btoa(`${username}:${password}`);
    
    try {
        const response = await fetch('/api/characters', {
            headers: {
                'Authorization': `Basic ${authToken}`
            }
        });
        
        if (response.ok) {
            // Store auth token in session storage
            window.sessionStorage.setItem('auth', authToken);
            
            // Add loading state to button
            const submitButton = event.target.querySelector('button[type="submit"]');
            submitButton.disabled = true;
            submitButton.innerHTML = `
                <i class="fas fa-spinner fa-spin"></i> Entering the Cosmos...
            `;
            
            // Redirect to main page after a short delay
            setTimeout(() => {
                window.location.href = 'index.html';
            }, 500);
        } else {
            showError('Invalid username or password');
        }
    } catch (error) {
        console.error('Login error:', error);
        showError('Failed to connect to server');
    }
});

function showError(message) {
    const form = document.getElementById('loginForm');
    
    // Remove any existing error messages
    const existingError = form.querySelector('.login-error');
    if (existingError) {
        existingError.remove();
    }
    
    // Create and add new error message
    const errorDiv = document.createElement('div');
    errorDiv.className = 'login-error mt-3 text-center';
    errorDiv.innerHTML = `
        <i class="fas fa-exclamation-circle"></i>
        <span>${message}</span>
    `;
    
    // Add shake animation
    errorDiv.style.animation = 'shake 0.5s ease-in-out';
    
    form.appendChild(errorDiv);
}

// Add shake animation keyframes
const style = document.createElement('style');
style.textContent = `
    @keyframes shake {
        0%, 100% { transform: translateX(0); }
        25% { transform: translateX(-10px); }
        75% { transform: translateX(10px); }
    }
`;
document.head.appendChild(style); 