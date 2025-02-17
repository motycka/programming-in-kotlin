import { CHARACTER_CLASSES, COMMON_DISPLAY_PROPERTIES, CLASS_SPECIFIC_PROPERTIES, showToast, fetchWithAuth } from '../script.js';

// Add the function here and export it
export function formatLevel(level) {
    // If level is a string like "LEVEL_1", extract the number
    if (typeof level === 'string' && level.startsWith('LEVEL_')) {
        return `Level ${level.split('_')[1]}`;
    }
    // If it's already a number or other format
    return `Level ${level}`;
}

class CharactersTab {
    constructor() {
        this.characterModal = null;
        this.levelUpModal = null;
        this.currentLevelUpCharacter = null;
        
        // Initialize immediately if DOM is ready, otherwise wait for DOMContentLoaded
        if (document.readyState === 'loading') {
            document.addEventListener('DOMContentLoaded', () => this.initialize());
        } else {
            this.initialize();
        }
    }

    async initialize() {
        console.log('Initializing characters tab...');
        
        // Initialize form and modals first
        this.initializeForm();
        this.characterModal = new bootstrap.Modal(document.getElementById('createCharacterModal'));
        this.levelUpModal = new bootstrap.Modal(document.getElementById('levelUpModal'));
        
        // Add event listeners
        const continueButton = document.getElementById('continueButton');
        const classSelect = document.getElementById('characterClass');
        const nameInput = document.getElementById('name');

        // Enable continue button when both class and name are filled
        const updateContinueButton = () => {
            continueButton.disabled = !classSelect.value || !nameInput.value;
        };

        classSelect.addEventListener('change', updateContinueButton);
        nameInput.addEventListener('input', updateContinueButton);
        continueButton.addEventListener('click', () => this.handleClassSelection());

        document.getElementById('characters-tab').addEventListener('shown.bs.tab', () => {
            console.log('Characters tab shown, reloading characters...');
            this.loadCharacters();
        });

        // Load characters immediately
        console.log('Loading initial characters...');
        await this.loadCharacters();
    }

    async loadCharacters() {
        console.log('Loading characters...');
        const listContainer = document.getElementById('charactersList');
        try {
            listContainer.innerHTML = '<div class="loading">Loading characters...</div>';
    
            const characters = await fetchWithAuth('/api/characters');
            
            console.log('Characters loaded:', characters);
            this.displayCharacters(characters);
        } catch (error) {
            console.error('Error loading characters:', error);
            listContainer.innerHTML = `
                <div class="empty-state">
                    <i class="fas fa-exclamation-circle fa-3x"></i>
                    <p>Failed to load characters</p>
                    <small>${error.message}</small>
                </div>
            `;
            showToast(error.message, true);
        }
    }

    displayCharacters(characters) {
        console.log('Displaying characters:', characters);
        const listContainer = document.getElementById('charactersList');
        listContainer.innerHTML = '';

        if (characters.length === 0) {
            listContainer.innerHTML = `
                <div class="empty-state">
                    <i class="fas fa-user-astronaut fa-3x"></i>
                    <p>Your cosmic journey begins here...</p>
                    <small>Create your first character to start your adventure</small>
                </div>
            `;
            return;
        }

        characters.forEach(character => {
            const characterElement = this.createCharacterElement(character);
            listContainer.appendChild(characterElement);
        });
    }

    initializeForm() {
        const classSelect = document.getElementById('characterClass');
    
        // Populate class selection
        Object.entries(CHARACTER_CLASSES).forEach(([key, classData]) => {
            const option = document.createElement('option');
            option.value = key;
            option.textContent = classData.name;
            classSelect.appendChild(option);
        });
    
        // Handle class selection change
        classSelect.addEventListener('change', this.updatePropertyInputs.bind(this));
    }

    updatePropertyInputs() {
        const selectedClass = document.getElementById('characterClass').value;
        const propertiesContainer = document.getElementById('dynamicProperties');
        propertiesContainer.innerHTML = '';
    
        if (!selectedClass) return;
    
        const classData = CHARACTER_CLASSES[selectedClass];
        const propertiesRow = document.createElement('div');
        propertiesRow.className = 'd-flex gap-3';
    
        Object.entries(classData.properties).forEach(([propName, config]) => {
            const formGroup = document.createElement('div');
            formGroup.className = 'flex-grow-1';
    
            const label = document.createElement('label');
            label.className = 'form-label';
            label.textContent = this.formatPropertyName(propName);
    
            const input = document.createElement('input');
            input.type = config.type;
            input.className = 'form-control';
            input.id = propName;
            input.name = propName;
            input.required = true;
            input.min = config.min;
            input.max = config.max;
            input.value = config.default;
    
            // Add input event listener for real-time validation
            input.addEventListener('input', () => {
                this.updateRemainingPoints();
                input.classList.toggle('is-invalid', parseInt(input.value) < config.min || parseInt(input.value) > config.max);
            });
    
            formGroup.appendChild(label);
            formGroup.appendChild(input);
            propertiesRow.appendChild(formGroup);
        });
    
        propertiesContainer.appendChild(propertiesRow);
        this.updateRemainingPoints();
    }

    updateRemainingPoints() {
        const selectedClass = document.getElementById('characterClass').value;
        if (!selectedClass) return;
    
        const inputs = document.querySelectorAll('#dynamicProperties input[type="number"]');
        const totalPoints = Array.from(inputs).reduce((sum, input) => sum + parseInt(input.value || 0), 0);
        const maxPoints = this.getPointsForLevel(1); // Level 1 points for new character
        const remainingPoints = maxPoints - totalPoints;
    
        let pointsDisplay = document.getElementById('pointsDisplay');
        if (!pointsDisplay) {
            pointsDisplay = document.createElement('div');
            pointsDisplay.id = 'pointsDisplay';
            document.getElementById('dynamicProperties').appendChild(pointsDisplay);
        }
    
        pointsDisplay.innerHTML = `
            <div class="points-summary ${
                remainingPoints === 0 ? 'points-valid' : 
                remainingPoints < 0 ? 'points-exceeded' : 
                'points-remaining'
            }">
                <div class="points-row">
                    <span>Points: ${totalPoints}/${maxPoints}</span>
                    <span>${remainingPoints >= 0 ? `${remainingPoints} remaining` : `${Math.abs(remainingPoints)} over limit`}</span>
                </div>
            </div>
        `;
    
        // Disable submit button if points don't match exactly
        const submitButton = document.querySelector('#characterForm button[type="submit"]');
        submitButton.disabled = remainingPoints !== 0;
    }

    validateCharacterPoints(properties) {
        const totalPoints = Object.values(properties).reduce((sum, value) => sum + value, 0);
        const level1Points = this.getPointsForLevel(1);
    
        if (totalPoints !== level1Points) {
            throw new Error(`Total character points must equal ${level1Points}. Current total: ${totalPoints}`);
        }
    }

    getPointsForLevel(level) {
        // This should match your backend logic for level points
        const BASE_POINTS = 200;
        const POINTS_PER_LEVEL = 50;
        return BASE_POINTS + (level - 1) * POINTS_PER_LEVEL;
    }

    async handleSubmit(event) {
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
            const totalPoints = Object.values(properties).reduce((sum, value) => sum + value, 0);
            const level1Points = this.getPointsForLevel(1);

            if (totalPoints !== level1Points) {
                throw new Error(`Total points must equal ${level1Points}. Current total: ${totalPoints}`);
            }

            // Add properties to form data
            Object.assign(formData, properties);

            submitButton.disabled = true;
            submitButton.textContent = 'Creating...';

            await this.createCharacter(formData);
            this.resetForm();
            showToast('Character created successfully!');
            await this.loadCharacters();
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

    async createCharacter(character) {
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

    createCharacterElement(character) {
        console.log('Creating character element:', character);
        const template = document.getElementById('character-template');
        const clone = template.content.cloneNode(true);
        const characterItem = clone.querySelector('.character-item');
        
        // Add character class as data attribute
        characterItem.dataset.class = character.characterClass;
        
        // Set character name and level
        characterItem.querySelector('.character-name').textContent = character.name;
        characterItem.querySelector('.level-value').textContent = formatLevel(character.level);
        
        // Add properties
        const propertiesContainer = characterItem.querySelector('.character-properties');
        
        // Add common properties first
        COMMON_DISPLAY_PROPERTIES.forEach(prop => {
            if (character[prop] !== undefined) {
                const propertyItem = this.createPropertyElement(prop, character[prop]);
                propertiesContainer.appendChild(propertyItem);
            }
        });

        // Add class-specific properties
        const classSpecificProps = CLASS_SPECIFIC_PROPERTIES[character.characterClass] || [];
        classSpecificProps.forEach(prop => {
            if (character[prop] !== undefined) {
                const propertyItem = this.createPropertyElement(prop, character[prop]);
                propertiesContainer.appendChild(propertyItem);
            }
        });

        // Add experience property with level up functionality
        const expPropertyItem = this.createPropertyElement('experience', character.experience);
        const isCharacterOwner = character.isOwner || character.owner;
        
        if (character.shouldLevelUp && isCharacterOwner) {
            expPropertyItem.classList.add('can-level-up');
            const label = expPropertyItem.querySelector('.property-label');
            label.textContent = `${this.formatPropertyName('experience')} ⇧`;
            expPropertyItem.addEventListener('click', () => this.showLevelUpModal(character));
        }
        
        propertiesContainer.appendChild(expPropertyItem);
        
        // Update class display to use icon with tooltip
        const classIcon = document.createElement('span');
        classIcon.className = 'class-icon';
        classIcon.setAttribute('title', CHARACTER_CLASSES[character.characterClass].name);
        classIcon.innerHTML = character.characterClass === 'WARRIOR' ? '⚔️' : '🔮';
        
        // Add the icon before the character name
        const nameElement = characterItem.querySelector('.character-name');
        nameElement.insertBefore(classIcon, nameElement.firstChild);
        
        // Initialize tooltip
        new bootstrap.Tooltip(classIcon);
        
        return characterItem;
    }

    showLevelUpModal(character) {
        const nextLevel = parseInt(character.level.split('_')[1]) + 1;
        this.currentLevelUpCharacter = character;
        
        this.showPointsAssignmentDialog(character, nextLevel, async (properties) => {
            try {
                const response = await fetchWithAuth(
                    `/api/characters/${character.id}/level-up`,
                    {
                        method: 'POST',
                        body: JSON.stringify(properties)
                    }
                );
                
                if (response.ok) {
                    this.levelUpModal.hide();
                    showToast('Character leveled up successfully!');
                    await this.loadCharacters();
                }
            } catch (error) {
                showToast(error.message, true);
            }
        });
    }

    showPointsAssignmentDialog(character, level, onSubmit) {
        // Make sure the modal is initialized
        if (!this.levelUpModal) {
            this.levelUpModal = new bootstrap.Modal(document.getElementById('levelUpModal'));
        }

        const modal = document.getElementById('levelUpModal');
        
        // Set character info in the modal
        const characterName = modal.querySelector('.character-info .character-name');
        const characterClass = modal.querySelector('.character-info .character-class');
        
        if (characterName && characterClass) {
            characterName.textContent = character.name || 'New Character';
            characterClass.textContent = CHARACTER_CLASSES[character.characterClass].name;
        }
        
        // Get properties based on character class
        const propertiesContainer = modal.querySelector('#levelUpProperties');
        if (!propertiesContainer) {
            console.error('Properties container not found');
            return;
        }
        propertiesContainer.innerHTML = '';
        
        // Create a flex row container for properties
        const propertiesRow = document.createElement('div');
        propertiesRow.className = 'd-flex gap-3';
        propertiesContainer.appendChild(propertiesRow);
        
        // Update submit button text based on whether it's a new character or level up
        const submitButton = modal.querySelector('button[type="submit"]');
        submitButton.textContent = character.id ? 'Confirm Level Up' : 'Create Character';

        // Create property inputs based on character class
        const propertiesToShow = character.characterClass === 'WARRIOR' 
            ? ['health', 'attackPower', 'stamina', 'defensePower']
            : ['health', 'attackPower', 'mana', 'healingPower'];
        
        propertiesToShow.forEach(prop => {
            const formGroup = document.createElement('div');
            formGroup.className = 'flex-grow-1';
            
            const label = document.createElement('label');
            label.className = 'form-label';
            label.textContent = this.formatPropertyName(prop);
            
            const input = document.createElement('input');
            input.type = 'number';
            input.className = 'form-control';
            input.id = `${prop}_points`;
            input.name = prop;
            input.value = character[prop] || 0;
            // For new characters, allow starting from 0
            input.min = character.id ? (character[prop] || 0) : 0;
            input.addEventListener('input', () => this.updateAssignedPoints(level));
            
            formGroup.appendChild(label);
            formGroup.appendChild(input);
            propertiesRow.appendChild(formGroup);
        });
        
        // Initial points display
        this.updateAssignedPoints(level);

        // Update form submission
        const form = modal.querySelector('form');
        form.onsubmit = async (event) => {
            event.preventDefault();
            const properties = {};
            const inputs = modal.querySelectorAll('#levelUpProperties input');
            inputs.forEach(input => {
                properties[input.name] = parseInt(input.value);
            });
            await onSubmit(properties);
        };
        
        // Show the modal
        this.levelUpModal.show();
    }

    updateAssignedPoints(level) {
        const inputs = document.querySelectorAll('#levelUpProperties input');
        const currentTotal = Array.from(inputs).reduce((sum, input) => sum + parseInt(input.value || 0), 0);
        const pointsForLevel = this.getPointsForLevel(level);
        
        const pointsDisplay = document.querySelector('#pointsDisplay');
        if (pointsDisplay) {
            pointsDisplay.innerHTML = `
                <div class="points-summary ${
                    currentTotal === pointsForLevel ? 'points-valid' : 
                    currentTotal > pointsForLevel ? 'points-exceeded' : 
                    'points-remaining'
                }">
                    <div class="points-row">
                        <span>Points: ${currentTotal}/${pointsForLevel}</span>
                        <span>${currentTotal <= pointsForLevel ? 
                            `${pointsForLevel - currentTotal} remaining` : 
                            `${currentTotal - pointsForLevel} over limit`}</span>
                    </div>
                </div>
            `;
        }

        // Update submit button state
        const submitButton = document.querySelector('#levelUpForm button[type="submit"]');
        if (submitButton) {
            submitButton.disabled = currentTotal !== pointsForLevel;
        }
    }

    async handleLevelUp(character) {
        try {
            // Show level up modal
            const modal = document.getElementById('levelUpModal');
            const form = modal.querySelector('#levelUpForm');
            
            // Store character data
            this.levelingCharacter = character;
            
            // Update modal content
            modal.querySelector('.character-name').textContent = character.name;
            modal.querySelector('.character-class').textContent = CHARACTER_CLASSES[character.characterClass].name;
            
            // Clear previous properties
            const propertiesContainer = modal.querySelector('#levelUpProperties');
            propertiesContainer.innerHTML = '';
            
            // Add property inputs
            const availableProperties = [...COMMON_DISPLAY_PROPERTIES];
            if (CLASS_SPECIFIC_PROPERTIES[character.characterClass]) {
                availableProperties.push(...CLASS_SPECIFIC_PROPERTIES[character.characterClass]);
            }
            
            availableProperties.forEach(prop => {
                if (prop !== 'experience' && prop !== 'level') {
                    const div = document.createElement('div');
                    div.className = 'mb-3';
                    div.innerHTML = `
                        <label class="form-label">${this.formatPropertyName(prop)}</label>
                        <input type="number" class="form-control" name="${prop}" value="0" min="0">
                    `;
                    propertiesContainer.appendChild(div);
                }
            });
            
            // Show available points
            const pointsDisplay = modal.querySelector('#pointsDisplay');
            pointsDisplay.innerHTML = `
                <div class="alert alert-info">
                    Available Points: <span id="availablePoints">${character.availablePoints}</span>
                </div>
            `;
            
            // Show the modal
            const levelUpModal = new bootstrap.Modal(modal);
            levelUpModal.show();
            
            // Handle form submission
            form.onsubmit = async (e) => {
                e.preventDefault();
                
                const formData = new FormData(form);
                const updates = {};
                let totalPoints = 0;
                
                formData.forEach((value, key) => {
                    const points = parseInt(value);
                    if (points > 0) {
                        updates[key] = points;
                        totalPoints += points;
                    }
                });
                
                if (totalPoints > character.availablePoints) {
                    showToast('Cannot allocate more points than available', true);
                    return;
                }
                
                try {
                    // Send PUT request to update character
                    const updatedCharacter = await fetchWithAuth(`/api/characters/${character.id}`, {
                        method: 'PUT',
                        body: JSON.stringify(updates)
                    });
                    
                    levelUpModal.hide();
                    showToast('Character leveled up successfully!');
                    
                    // Refresh characters list
                    await this.loadCharacters();
                } catch (error) {
                    console.error('Error leveling up character:', error);
                    showToast(error.message, true);
                }
            };
            
            // Update available points display when inputs change
            const inputs = form.querySelectorAll('input[type="number"]');
            inputs.forEach(input => {
                input.addEventListener('change', () => {
                    let used = 0;
                    inputs.forEach(i => used += parseInt(i.value) || 0);
                    const available = character.availablePoints - used;
                    modal.querySelector('#availablePoints').textContent = available;
                    
                    // Disable submit if points exceeded
                    form.querySelector('button[type="submit"]').disabled = available < 0;
                });
            });
            
        } catch (error) {
            console.error('Error showing level up modal:', error);
            showToast(error.message, true);
        }
    }

    formatPropertyName(prop) {
        // Convert camelCase to Title Case and remove "Power"
        return prop
            .replace(/Power/g, '')  // Remove "Power" from the property name
            .replace(/([A-Z])/g, ' $1')  // Add space before capital letters
            .replace(/^./, str => str.toUpperCase())  // Capitalize first letter
            .trim();  // Remove any extra spaces
    }

    resetForm() {
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
        this.characterModal.hide();
    }

    createPropertyElement(prop, value) {
        const propertyItem = document.createElement('div');
        propertyItem.className = 'property-item';
        propertyItem.dataset.property = prop;
        
        const label = document.createElement('div');
        label.className = 'property-label';
        label.textContent = this.formatPropertyName(prop);
        
        const valueElement = document.createElement('div');
        valueElement.className = 'property-value';
        valueElement.textContent = value;
        
        propertyItem.appendChild(label);
        propertyItem.appendChild(valueElement);
        
        return propertyItem;
    }

    handleClassSelection() {
        const selectedClass = document.getElementById('characterClass').value;
        const characterName = document.getElementById('name').value;
        
        if (!selectedClass || !characterName) return;
        
        // Create initial character with default values
        const classData = CHARACTER_CLASSES[selectedClass];
        const character = {
            name: characterName,
            characterClass: selectedClass,
            // Initialize all properties with their default values
            ...Object.entries(classData.properties).reduce((acc, [prop, config]) => ({
                ...acc,
                [prop]: config.default || 0
            }), {})
        };
        
        // Hide the character creation modal
        this.characterModal.hide();
        
        // Update modal title for character creation
        const modal = document.getElementById('levelUpModal');
        const modalTitle = modal.querySelector('.modal-title');
        modalTitle.textContent = 'Assign Character Points';
        
        // Show the points assignment dialog
        this.showPointsAssignmentDialog(character, 1, async (properties) => {
            try {
                const formData = {
                    name: character.name,
                    characterClass: character.characterClass,
                    ...properties
                };
                
                await this.createCharacter(formData);
                this.levelUpModal.hide();
                showToast('Character created successfully!');
                await this.loadCharacters();
            } catch (error) {
                showToast(error.message, true);
            }
        });
    }
}

export default CharactersTab; 