// Add a toast notification system
function showToast(message, isError = false) {
    const toast = document.createElement('div');
    toast.className = `toast ${isError ? 'toast-error' : 'toast-success'}`;
    toast.textContent = message;
    document.body.appendChild(toast);
    
    // Remove the toast after 3 seconds
    setTimeout(() => {
        toast.remove();
    }, 3000);
}

// Add this at the beginning of the file
function checkAuth() {
    const auth = window.sessionStorage.getItem('auth');
    if (!auth) {
        window.location.href = '/login.html';
        return false;
    }
    return true;
}

// Update fetchWithAuth function
async function fetchWithAuth(url, options = {}) {
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
        
        if (response.status === 401) {
            window.sessionStorage.removeItem('auth');
            window.location.href = '/login.html';
            throw new Error('Session expired');
        }
        
        if (!response.ok) {
            let errorMessage = `Error: ${response.status}`;
            try {
                const errorData = await response.json();
                errorMessage = errorData.message || errorMessage;
            } catch (e) {
                errorMessage = response.statusText;
            }
            throw new Error(errorMessage);
        }
        
        return response;
    } catch (error) {
        showToast(error.message, true);
        throw error;
    }
}

// Add common and class-specific properties configuration
const COMMON_DISPLAY_PROPERTIES = ['health', 'attackPower'];
const CLASS_SPECIFIC_PROPERTIES = {
    WARRIOR: ['stamina', 'defensePower'],
    SORCERER: ['mana', 'healingPower']
};

// Character properties configuration for each class
const CHARACTER_CLASSES = {
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

// Add character level points configuration
const CHARACTER_LEVEL_POINTS = {
    LEVEL_1: 200,
    LEVEL_2: 210,
    LEVEL_3: 230,
    LEVEL_4: 260,
    LEVEL_5: 300,
    LEVEL_6: 350,
    LEVEL_7: 410,
    LEVEL_8: 480,
    LEVEL_9: 560,
    LEVEL_10: 650
};

// Add validation function
function validateCharacterPoints(properties) {
    const totalPoints = Object.values(properties).reduce((sum, value) => sum + value, 0);
    const level1Points = CHARACTER_LEVEL_POINTS.LEVEL_1;
    
    if (totalPoints !== level1Points) {
        throw new Error(`Total character points must equal ${level1Points}. Current total: ${totalPoints}`);
    }
}

// Update form to show remaining points
function updateRemainingPoints() {
    const selectedClass = document.getElementById('characterClass').value;
    if (!selectedClass) return;

    const inputs = document.querySelectorAll('#dynamicProperties input[type="number"]');
    const totalPoints = Array.from(inputs).reduce((sum, input) => sum + parseInt(input.value || 0), 0);
    const maxPoints = CHARACTER_LEVEL_POINTS.LEVEL_1;
    const remainingPoints = maxPoints - totalPoints;

    let pointsDisplay = document.getElementById('pointsDisplay');
    if (!pointsDisplay) {
        pointsDisplay = document.createElement('div');
        pointsDisplay.id = 'pointsDisplay';
        pointsDisplay.className = 'alert mt-3';
        document.getElementById('dynamicProperties').appendChild(pointsDisplay);
    }

    pointsDisplay.className = `alert mt-3 ${remainingPoints === 0 ? 'alert-success' : remainingPoints < 0 ? 'alert-danger' : 'alert-info'}`;
    pointsDisplay.textContent = `Points: ${totalPoints}/${maxPoints} (${remainingPoints >= 0 ? 'Remaining: ' + remainingPoints : 'Exceeded by ' + Math.abs(remainingPoints)})`;
}

// Initialize the form
function initializeForm() {
    const classSelect = document.getElementById('characterClass');
    
    // Populate class selection
    Object.entries(CHARACTER_CLASSES).forEach(([key, classData]) => {
        const option = document.createElement('option');
        option.value = key;
        option.textContent = classData.name;
        classSelect.appendChild(option);
    });

    // Handle class selection change
    classSelect.addEventListener('change', updatePropertyInputs);
}

function updatePropertyInputs() {
    const selectedClass = document.getElementById('characterClass').value;
    const propertiesContainer = document.getElementById('dynamicProperties');
    propertiesContainer.innerHTML = ''; // Clear existing properties

    if (!selectedClass) return;

    const classData = CHARACTER_CLASSES[selectedClass];
    const propertiesRow = document.createElement('div');
    propertiesRow.className = 'd-flex gap-3';
    
    Object.entries(classData.properties).forEach(([propName, config]) => {
        const formGroup = document.createElement('div');
        formGroup.className = 'flex-grow-1';
        
        const label = document.createElement('label');
        label.className = 'form-label';
        // Format label text
        let labelText = propName;
        if (propName === 'attackPower') labelText = 'Attack';
        else if (propName === 'healingPower') labelText = 'Healing';
        else if (propName === 'defensePower') labelText = 'Defense';
        
        label.textContent = labelText.charAt(0).toUpperCase() + labelText.slice(1);
        
        const input = document.createElement('input');
        input.type = config.type;
        input.className = 'form-control';
        input.id = propName;
        input.name = propName;
        input.required = true;
        input.min = config.min;
        input.max = config.max;
        input.value = config.default;

        // Add event listener for point calculation
        input.addEventListener('input', updateRemainingPoints);

        formGroup.appendChild(label);
        formGroup.appendChild(input);
        propertiesRow.appendChild(formGroup);
    });

    propertiesContainer.appendChild(propertiesRow);
    updateRemainingPoints(); // Initialize points display
}

// Add these variables at the top
let characterModal = null;
let matchResultModal = null;
let newMatchModal = null;
let currentUser = null;
const loginModal = null; // Will be initialized in DOMContentLoaded
let levelUpModal = null;
let currentLevelUpCharacter = null;

// Update the initialization code
document.addEventListener('DOMContentLoaded', () => {
    if (!checkAuth()) return;
    
    initializeForm();
    document.getElementById('characterForm').addEventListener('submit', handleSubmit);
    loadCharacters();
    
    // Initialize the modals
    characterModal = new bootstrap.Modal(document.getElementById('createCharacterModal'));
    matchResultModal = new bootstrap.Modal(document.getElementById('matchResultModal'));
    newMatchModal = new bootstrap.Modal(document.getElementById('newMatchModal'));
    
    // Initialize all tabs
    initializeMatchForm();
    initializeMatchesTab();
    initializeLeaderboardTab();
    
    // Initialize login modal
    loginModal = new bootstrap.Modal(document.getElementById('loginModal'));
    document.getElementById('loginForm').addEventListener('submit', handleLogin);
    
    // Initialize level up modal
    levelUpModal = new bootstrap.Modal(document.getElementById('levelUpModal'));
    document.getElementById('levelUpForm').addEventListener('submit', handleLevelUp);
    
    // Show login modal on page load if not logged in
    showLoginModal();
});

// Update the resetForm function to close the modal
function resetForm() {
    const form = document.getElementById('characterForm');
    form.reset();
    
    // Reset class selection
    const classSelect = document.getElementById('characterClass');
    classSelect.value = '';
    
    // Clear dynamic properties
    const propertiesContainer = document.getElementById('dynamicProperties');
    propertiesContainer.innerHTML = '';
    
    // Remove points display
    const pointsDisplay = document.getElementById('pointsDisplay');
    if (pointsDisplay) {
        pointsDisplay.remove();
    }
    
    // Remove any error messages
    const errorDisplay = document.getElementById('formError');
    if (errorDisplay) {
        errorDisplay.remove();
    }

    // Close the modal
    characterModal.hide();
}

// Update handleSubmit to prevent modal from closing on validation error
async function handleSubmit(event) {
    event.preventDefault();
    const form = event.target;
    const submitButton = form.querySelector('button[type="submit"]');
    const errorDisplay = document.getElementById('formError');
    
    // Clear previous error
    if (errorDisplay) {
        errorDisplay.remove();
    }
    
    const selectedClass = document.getElementById('characterClass').value;
    if (!selectedClass) return;

    const formData = {
        name: document.getElementById('name').value,
        characterClass: selectedClass
    };

    // Add class-specific properties
    const properties = {};
    Object.keys(CHARACTER_CLASSES[selectedClass].properties).forEach(prop => {
        properties[prop] = parseInt(document.getElementById(prop).value);
    });

    try {
        // Validate total points
        validateCharacterPoints(properties);
        
        // Add properties to form data
        Object.assign(formData, properties);

        submitButton.disabled = true;
        submitButton.textContent = 'Creating...';
        
        await createCharacter(formData);
        characterModal.hide();
        form.reset();
        showToast('Character created successfully!');
        await loadCharacters();
    } catch (error) {
        console.error('Error creating character:', error);
        
        // Display error message
        const errorDiv = document.createElement('div');
        errorDiv.id = 'formError';
        errorDiv.className = 'alert alert-danger mt-3';
        errorDiv.textContent = error.message;
        form.querySelector('.modal-body').appendChild(errorDiv);
        
        showToast(error.message, true);
    } finally {
        submitButton.disabled = false;
        submitButton.textContent = 'Create Character';
    }
}

function formatLevel(levelString) {
    if (!levelString) return 'Level 1';
    // Convert "LEVEL_1" to "Level 1"
    return levelString.toLowerCase()
        .replace('level_', 'Level ')
        .replace(/\b\w/g, char => char.toUpperCase());
}

// Function to get properties in the desired order
function getOrderedProperties(character) {
    const commonProps = COMMON_DISPLAY_PROPERTIES.map(prop => ({
        name: prop,
        label: prop === 'attackPower' ? 'Attack' : prop.charAt(0).toUpperCase() + prop.slice(1),
        value: character[prop]
    }));

    let classSpecificProps = [];
    if (character.characterClass === 'WARRIOR') {
        classSpecificProps = [
            { name: 'stamina', label: 'Stamina', value: character.stamina },
            { name: 'defensePower', label: 'Defense', value: character.defensePower }
        ];
    } else if (character.characterClass === 'SORCERER') {
        classSpecificProps = [
            { name: 'mana', label: 'Mana', value: character.mana },
            { name: 'healingPower', label: 'Healing', value: character.healingPower }
        ];
    }

    // Add experience last
    const experienceProp = {
        name: 'experience',
        label: 'Experience',
        value: `${character.experience || 0} XP`
    };

    return [...commonProps, ...classSpecificProps, experienceProp];
}

// Update the displayCharacters function
function displayCharacters(characters) {
    const list = document.getElementById('charactersList');
    list.innerHTML = characters.length ? '' : '<div class="no-characters">No characters found</div>';
    
    characters.forEach(character => {
        const template = document.getElementById('character-template');
        const clone = template.content.cloneNode(true);
        
        // Set character name, class and level
        clone.querySelector('.character-name').textContent = `${character.name} · ${CHARACTER_CLASSES[character.characterClass]?.name || character.characterClass}`;
        clone.querySelector('.level-value').textContent = formatLevel(character.level);

        // Create property grid
        const propertiesContainer = clone.querySelector('.character-properties');
        
        // Display properties in the desired order
        getOrderedProperties(character).forEach(prop => {
            const propertyDiv = document.createElement('div');
            propertyDiv.className = 'property-item';
            propertyDiv.setAttribute('data-property', prop.name);
            
            propertyDiv.innerHTML = `
                <div class="property-label">${prop.label}</div>
                <div class="property-value">${prop.value || 0}</div>
            `;
            propertiesContainer.appendChild(propertyDiv);
        });

        list.appendChild(clone);
    });
}

async function loadCharacters() {
    const listContainer = document.getElementById('charactersList');
    try {
        listContainer.innerHTML = '<div class="loading">Loading characters...</div>';
        
        const response = await fetchWithAuth('/api/characters');
        const characters = await response.json();
        displayCharacters(characters);
    } catch (error) {
        console.error('Error loading characters:', error);
        listContainer.innerHTML = '<div class="error">Failed to load characters. Please try again.</div>';
    }
}

async function createCharacter(character) {
    const response = await fetchWithAuth('/api/characters', {
        method: 'POST',
        body: JSON.stringify(character),
    });
    
    const data = await response.json();
    if (!response.ok) {
        throw new Error(data.message || 'Failed to create character');
    }
    return data;
}

async function updateCharacter(id, character) {
    const response = await fetchWithAuth(`/api/characters/${id}`, {
        method: 'PUT',
        body: JSON.stringify(character),
    });
    return response.json();
}

async function deleteCharacter(id) {
    await fetchWithAuth(`/api/characters/${id}`, {
        method: 'DELETE',
    });
}

// Update initializeMatchTab function to initializeMatchForm
function initializeMatchForm() {
    const challengerSelect = document.getElementById('challengerSelect');
    const opponentSelect = document.getElementById('opponentSelect');
    const fightButton = document.getElementById('fightButton');
    const randomMatchButton = document.getElementById('randomMatchButton');

    // Add event listeners
    challengerSelect.addEventListener('change', updateCharacterStats);
    opponentSelect.addEventListener('change', updateCharacterStats);
    
    // Update random match button handler
    randomMatchButton.addEventListener('click', () => {
        const challengerOptions = Array.from(challengerSelect.options).slice(1); // Skip the first "Select" option
        const opponentOptions = Array.from(opponentSelect.options).slice(1);

        if (challengerOptions.length === 0 || opponentOptions.length === 0) {
            showToast('Not enough characters for a random match', true);
            return;
        }

        // Select random challenger and opponent
        const randomChallenger = challengerOptions[Math.floor(Math.random() * challengerOptions.length)];
        const randomOpponent = opponentOptions[Math.floor(Math.random() * opponentOptions.length)];

        challengerSelect.value = randomChallenger.value;
        opponentSelect.value = randomOpponent.value;

        // Trigger change events to update stats
        challengerSelect.dispatchEvent(new Event('change'));
        opponentSelect.dispatchEvent(new Event('change'));
    });

    // Update the fight button click handler
    fightButton.addEventListener('click', async () => {
        try {
            fightButton.disabled = true;
            fightButton.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Fighting...';
            
            const challenger = JSON.parse(challengerSelect.value);
            const opponent = JSON.parse(opponentSelect.value);
            
            const result = await submitMatch(challenger.id, opponent.id);
            showToast('Match completed successfully!');
            
            // Close the new match modal
            newMatchModal.hide();
            
            // Display match result modal
            displayMatchResult(result);
            
            // Reset selections and stats
            challengerSelect.value = '';
            opponentSelect.value = '';
            document.getElementById('challengerStats').innerHTML = '';
            document.getElementById('opponentStats').innerHTML = '';
            
            // Switch to matches tab and reload matches list
            document.getElementById('matches-tab').click();
            await loadMatches();
            
        } catch (error) {
            console.error('Error in match:', error);
            showToast(error.message, true);
        } finally {
            fightButton.disabled = false;
            fightButton.innerHTML = '<i class="fas fa-swords"></i> Fight!';
        }
    });

    // Load characters when modal is opened
    document.getElementById('newMatchModal').addEventListener('show.bs.modal', loadMatchCharacters);
}

// Update the loadMatchCharacters function
async function loadMatchCharacters() {
    try {
        // Load challengers and opponents in parallel
        const [challengersResponse, opponentsResponse] = await Promise.all([
            fetchWithAuth('/api/characters/challengers'),
            fetchWithAuth('/api/characters/opponents')
        ]);
        
        const challengers = await challengersResponse.json();
        const opponents = await opponentsResponse.json();
        
        const challengerSelect = document.getElementById('challengerSelect');
        const opponentSelect = document.getElementById('opponentSelect');
        
        // Clear existing options except the first one
        challengerSelect.innerHTML = '<option value="">Select challenger...</option>';
        opponentSelect.innerHTML = '<option value="">Select opponent...</option>';
        
        // Add challenger options
        challengers.forEach(character => {
            const option = document.createElement('option');
            option.value = JSON.stringify(character);
            option.textContent = `${character.name} (${CHARACTER_CLASSES[character.characterClass]?.name || character.characterClass} - ${formatLevel(character.level)})`;
            challengerSelect.appendChild(option);
        });

        // Add opponent options
        opponents.forEach(character => {
            const option = document.createElement('option');
            option.value = JSON.stringify(character);
            option.textContent = `${character.name} (${CHARACTER_CLASSES[character.characterClass]?.name || character.characterClass} - ${formatLevel(character.level)})`;
            opponentSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error loading characters for match:', error);
        showToast('Failed to load characters for match', true);
    }
}

function updateCharacterStats() {
    const challengerSelect = document.getElementById('challengerSelect');
    const opponentSelect = document.getElementById('opponentSelect');
    const fightButton = document.getElementById('fightButton');
    
    // Update challenger stats
    if (challengerSelect.value) {
        const challenger = JSON.parse(challengerSelect.value);
        displayCharacterStats('challengerStats', challenger);
    } else {
        document.getElementById('challengerStats').innerHTML = '';
    }
    
    // Update opponent stats
    if (opponentSelect.value) {
        const opponent = JSON.parse(opponentSelect.value);
        displayCharacterStats('opponentStats', opponent);
    } else {
        document.getElementById('opponentStats').innerHTML = '';
    }
    
    // Enable/disable fight button
    fightButton.disabled = !(challengerSelect.value && opponentSelect.value);
}

function displayCharacterStats(containerId, character) {
    const container = document.getElementById(containerId);
    container.innerHTML = '';
    
    getOrderedProperties(character).forEach(prop => {
        const propertyDiv = document.createElement('div');
        propertyDiv.className = 'property-item';
        propertyDiv.setAttribute('data-property', prop.name);
        
        propertyDiv.innerHTML = `
            <div class="property-label">${prop.label}</div>
            <div class="property-value">${prop.value || 0}</div>
        `;
        container.appendChild(propertyDiv);
    });
}

// Update submitMatch function to return the match result directly
async function submitMatch(challengerId, opponentId) {
    const response = await fetchWithAuth('/api/matches', {
        method: 'POST',
        body: JSON.stringify({
            rounds: 100,
            challengerId: challengerId,
            opponentId: opponentId
        }),
    });
    
    const data = await response.json();
    if (!response.ok) {
        throw new Error(data.message || 'Failed to start match');
    }
    return data; // This is the match result
}

function displayMatchResult(result) {
    const modal = document.getElementById('matchResultModal');
    
    // Set challenger info
    modal.querySelector('.challenger-name').textContent = result.challenger.name;
    modal.querySelector('.challenger-result').textContent = result.challenger.isVictor ? 'VICTOR' : 'DEFEATED';
    modal.querySelector('.challenger-result').className = `badge ${result.challenger.isVictor ? 'victor' : 'defeated'}`;
    
    // Set opponent info
    modal.querySelector('.opponent-name').textContent = result.opponent.name;
    modal.querySelector('.opponent-result').textContent = result.opponent.isVictor ? 'VICTOR' : 'DEFEATED';
    modal.querySelector('.opponent-result').className = `badge ${result.opponent.isVictor ? 'victor' : 'defeated'}`;
    
    // Display character stats
    displayCharacterStats('challenger-stats', result.challenger);
    displayCharacterStats('opponent-stats', result.opponent);
    
    // Display rounds
    const roundsList = modal.querySelector('.rounds-list');
    roundsList.innerHTML = '';
    
    result.rounds.forEach(round => {
        const roundDiv = document.createElement('div');
        const isChallenger = round.characterId === result.challenger.id;
        roundDiv.className = `round-item ${isChallenger ? 'challenger-action' : 'opponent-action'}`;
        
        let roundContent = `<strong>Round ${round.round}:</strong> `;
        roundContent += `${isChallenger ? result.challenger.name : result.opponent.name}'s turn<br>`;
        
        // Add stat changes
        const changes = [];
        if (round.healthDelta !== 0) {
            changes.push(`Health: <span class="stat-change ${round.healthDelta > 0 ? 'positive' : 'negative'}">${round.healthDelta > 0 ? '+' : ''}${round.healthDelta}</span>`);
        }
        if (round.staminaDelta !== 0) {
            changes.push(`Stamina: <span class="stat-change ${round.staminaDelta > 0 ? 'positive' : 'negative'}">${round.staminaDelta > 0 ? '+' : ''}${round.staminaDelta}</span>`);
        }
        if (round.manaDelta !== 0) {
            changes.push(`Mana: <span class="stat-change ${round.manaDelta > 0 ? 'positive' : 'negative'}">${round.manaDelta > 0 ? '+' : ''}${round.manaDelta}</span>`);
        }
        
        roundContent += changes.join(' • ');
        roundDiv.innerHTML = roundContent;
        roundsList.appendChild(roundDiv);
    });
    
    matchResultModal.show();
}

// Add this helper function
function getRandomPair(min, max) {
    const first = Math.floor(Math.random() * (max - min + 1)) + min;
    let second;
    do {
        second = Math.floor(Math.random() * (max - min + 1)) + min;
    } while (second === first);
    
    return [first, second];
}

// Add this function to initialize matches tab
function initializeMatchesTab() {
    // Load matches when tab is shown
    document.getElementById('matches-tab').addEventListener('shown.bs.tab', loadMatches);
}

// Add this function to load matches
async function loadMatches() {
    const matchesList = document.getElementById('matchesList');
    try {
        const response = await fetchWithAuth('/api/matches');
        const matches = await response.json();
        
        if (matches.length === 0) {
            matchesList.innerHTML = '<p class="text-muted text-center">No matches found</p>';
            return;
        }

        matchesList.innerHTML = '';
        matches.forEach(match => {
            const matchElement = createMatchElement(match);
            matchesList.appendChild(matchElement);
        });
    } catch (error) {
        console.error('Error loading matches:', error);
        matchesList.innerHTML = '<p class="text-danger text-center">Failed to load matches</p>';
    }
}

// Add this function to create match elements
function createMatchElement(match) {
    const div = document.createElement('div');
    div.className = 'match-item';
    
    div.innerHTML = `
        <div class="match-header">
            <div class="match-participant">
                <div class="match-result ${match.challenger.isVictor ? 'winner' : 'loser'}">
                    ${match.challenger.character.name}
                </div>
                <div class="class">
                    ${CHARACTER_CLASSES[match.challenger.character.characterClass]?.name || match.challenger.character.characterClass}
                </div>
            </div>
            <div class="match-vs">VS</div>
            <div class="match-participant">
                <div class="match-result ${match.opponent.isVictor ? 'winner' : 'loser'}">
                    ${match.opponent.character.name}
                </div>
                <div class="class">
                    ${CHARACTER_CLASSES[match.opponent.character.characterClass]?.name || match.opponent.character.characterClass}
                </div>
            </div>
        </div>
        <div class="match-footer">
            <span>${match.rounds.length} rounds</span>
            <span>Experience: ${match.challenger.character.experience} / ${match.opponent.character.experience}</span>
        </div>
    `;

    // Add click handler to show match details
    div.addEventListener('click', () => {
        displayMatchResult(match);
    });
    
    return div;
}

// Add these functions for login handling
function showLoginModal() {
    if (loginModal) {
        loginModal.show();
    }
}

async function handleLogin(event) {
    event.preventDefault();
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    
    try {
        // Test the credentials
        const response = await fetch('/api/characters', {
            headers: {
                'Authorization': 'Basic ' + btoa(`${username}:${password}`)
            }
        });
        
        if (response.ok) {
            currentUser = { username, password };
            loginModal.hide();
            document.getElementById('loginForm').reset();
            showToast('Login successful!');
            
            // Reload the current view
            await loadCharacters();
        } else {
            throw new Error('Invalid credentials');
        }
    } catch (error) {
        showToast(error.message, true);
    }
}

// Add this function to initialize leaderboard tab
function initializeLeaderboardTab() {
    // Load leaderboard when tab is shown
    document.getElementById('leaderboard-tab').addEventListener('shown.bs.tab', loadLeaderboard);
}

// Add this function to load leaderboard data
async function loadLeaderboard() {
    const leaderboardContent = document.getElementById('leaderboardContent');
    try {
        const response = await fetchWithAuth('/api/scores');
        const leaderboard = await response.json();
        
        if (leaderboard.length === 0) {
            leaderboardContent.innerHTML = '<p class="text-muted text-center">No leaderboard data available</p>';
            return;
        }

        // Create table
        const table = document.createElement('table');
        table.className = 'table table-hover leaderboard-table';
        
        // Add table header
        table.innerHTML = `
            <thead>
                <tr>
                    <th>Position</th>
                    <th>Character</th>
                    <th>Class</th>
                    <th>Level</th>
                    <th>W/L/D</th>
                    <th>Win Rate</th>
                </tr>
            </thead>
            <tbody>
                ${leaderboard.map(entry => {
                    const character = entry.character.character;
                    const stats = entry.character;
                    const totalGames = stats.wins + stats.losses + stats.draws;
                    const winRate = totalGames > 0 
                        ? ((stats.wins / totalGames) * 100).toFixed(1) 
                        : '0.0';
                    
                    return `
                        <tr>
                            <td class="position">
                                ${entry.position <= 3 
                                    ? `<i class="fas fa-trophy trophy-${entry.position}"></i>` 
                                    : entry.position}
                            </td>
                            <td>${character.name}</td>
                            <td>${CHARACTER_CLASSES[character.characterClass]?.name || character.characterClass}</td>
                            <td>${formatLevel(character.level)}</td>
                            <td>${stats.wins}/${stats.losses}/${stats.draws}</td>
                            <td>${winRate}%</td>
                        </tr>
                    `;
                }).join('')}
            </tbody>
        `;
        
        leaderboardContent.innerHTML = '';
        leaderboardContent.appendChild(table);
    } catch (error) {
        console.error('Error loading leaderboard:', error);
        leaderboardContent.innerHTML = '<p class="text-danger text-center">Failed to load leaderboard</p>';
    }
}

// Update the createCharacterElement function
function createCharacterElement(character) {
    const template = document.getElementById('character-template');
    const clone = template.content.cloneNode(true);
    const characterItem = clone.querySelector('.character-item');
    
    // Set character name and level
    characterItem.querySelector('.character-name').textContent = character.name;
    characterItem.querySelector('.level-value').textContent = formatLevel(character.level);
    
    // Show level up indicator if character should level up and is owned by current user
    const levelUpIndicator = characterItem.querySelector('.level-up-indicator');
    if (character.shouldLevelUp) {
        levelUpIndicator.classList.remove('d-none');
        const levelUpButton = levelUpIndicator.querySelector('.level-up-button');
        levelUpButton.addEventListener('click', () => showLevelUpModal(character));
    }
    
    // Add properties
    const propertiesContainer = characterItem.querySelector('.character-properties');
    
    // Add common properties first
    COMMON_DISPLAY_PROPERTIES.forEach(prop => {
        if (character[prop] !== undefined) {
            const propertyItem = createPropertyElement(prop, character[prop]);
            propertiesContainer.appendChild(propertyItem);
        }
    });

    // Add class-specific properties
    const classSpecificProps = CLASS_SPECIFIC_PROPERTIES[character.characterClass] || [];
    classSpecificProps.forEach(prop => {
        if (character[prop] !== undefined) {
            const propertyItem = createPropertyElement(prop, character[prop]);
            propertiesContainer.appendChild(propertyItem);
        }
    });

    // Add experience property
    const expPropertyItem = createPropertyElement('experience', character.experience);
    propertiesContainer.appendChild(expPropertyItem);

    return characterItem;
}

// Helper function to create property elements
function createPropertyElement(property, value) {
    const div = document.createElement('div');
    div.className = 'property-item';
    div.dataset.property = property;
    div.innerHTML = `
        <div class="property-label">${property}</div>
        <div class="property-value">${value}</div>
    `;
    return div;
}

function getCurrentUsername() {
    const auth = window.sessionStorage.getItem('auth');
    if (auth) {
        return atob(auth).split(':')[0];
    }
    return null;
}

function showLevelUpModal(character) {
    currentLevelUpCharacter = character;
    const propertiesContainer = document.getElementById('levelUpProperties');
    propertiesContainer.innerHTML = '';
    
    // Get available properties based on character class
    const properties = CHARACTER_CLASSES[character.characterClass].properties;
    
    // Create property adjusters
    Object.entries(properties).forEach(([prop, config]) => {
        const div = document.createElement('div');
        div.className = 'property-adjuster';
        div.innerHTML = `
            <button type="button" class="btn btn-outline-secondary" onclick="adjustProperty('${prop}', -1)">-</button>
            <div class="form-floating">
                <input type="number" class="form-control" id="${prop}_levelup" value="0" readonly>
                <label for="${prop}_levelup">${prop}</label>
            </div>
            <button type="button" class="btn btn-outline-secondary" onclick="adjustProperty('${prop}', 1)">+</button>
        `;
        propertiesContainer.appendChild(div);
    });
    
    // Reset points
    document.getElementById('pointsLeft').textContent = '10';
    
    levelUpModal.show();
}

function adjustProperty(prop, delta) {
    const input = document.getElementById(`${prop}_levelup`);
    const pointsLeft = document.getElementById('pointsLeft');
    const currentPoints = parseInt(pointsLeft.textContent);
    
    if (delta > 0 && currentPoints <= 0) return;
    if (delta < 0 && parseInt(input.value) <= 0) return;
    
    input.value = parseInt(input.value) + delta;
    pointsLeft.textContent = currentPoints - delta;
}

async function handleLevelUp(event) {
    event.preventDefault();
    
    if (!currentLevelUpCharacter) return;
    
    const properties = {};
    const propertiesContainer = document.getElementById('levelUpProperties');
    propertiesContainer.querySelectorAll('input').forEach(input => {
        properties[input.id.replace('_levelup', '')] = parseInt(input.value);
    });
    
    try {
        const response = await fetchWithAuth(`/api/characters/${currentLevelUpCharacter.id}/level-up`, {
            method: 'POST',
            body: JSON.stringify(properties)
        });
        
        if (response.ok) {
            showToast('Character leveled up successfully!');
            levelUpModal.hide();
            loadCharacters(); // Reload the characters list
        }
    } catch (error) {
        showToast(error.message, true);
    }
}