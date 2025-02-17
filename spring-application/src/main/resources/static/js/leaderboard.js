import { fetchWithAuth, CHARACTER_CLASSES } from '../script.js';
import { formatLevel } from './characters.js';

class LeaderboardTab {
    constructor() {
        this.rankings = [];
        this.currentFilter = 'ALL';
        this.initialize();
    }

    initialize() {
        console.log('Initializing leaderboard tab...');
        
        // Add event listener for tab shown
        document.getElementById('leaderboard-tab').addEventListener('shown.bs.tab', () => {
            console.log('Leaderboard tab shown, loading rankings...');
            this.loadLeaderboard();
        });
    }

    async loadLeaderboard() {
        const leaderboardContainer = document.getElementById('leaderboardContent');
        try {
            leaderboardContainer.innerHTML = `
                <div class="leaderboard-loading">
                    <i class="fas fa-spinner fa-spin fa-2x"></i>
                    <p>Loading rankings...</p>
                </div>
            `;

            this.rankings = await fetchWithAuth('/api/leaderboards');
            this.displayLeaderboard();
        } catch (error) {
            console.error('Error loading leaderboard:', error);
            leaderboardContainer.innerHTML = `
                <div class="leaderboard-error">
                    <i class="fas fa-exclamation-circle fa-3x"></i>
                    <p>Failed to load leaderboard</p>
                    <small>${error.message}</small>
                </div>
            `;
        }
    }

    displayLeaderboard() {
        const leaderboardContainer = document.getElementById('leaderboardContent');
        
        if (!this.rankings || this.rankings.length === 0) {
            leaderboardContainer.innerHTML = `
                <div class="empty-state">
                    <i class="fas fa-trophy fa-3x"></i>
                    <p>No rankings yet</p>
                    <small>Battle to claim your place in the cosmos!</small>
                </div>
            `;
            return;
        }

        // Filter rankings based on current filter
        const filteredRankings = this.currentFilter === 'ALL' 
            ? this.rankings
            : this.rankings.filter(char => char.characterClass === this.currentFilter);

        // Create the table with filter buttons and the existing structure
        leaderboardContainer.innerHTML = `
            <div class="leaderboard-filters mb-4">
                <div class="btn-group">
                    <button class="btn ${this.currentFilter === 'ALL' ? 'btn-cosmic active' : 'btn-cosmic-outline'}" 
                            data-filter="ALL">
                        🌟 All Classes
                    </button>
                    <button class="btn ${this.currentFilter === 'WARRIOR' ? 'btn-cosmic active' : 'btn-cosmic-outline'}" 
                            data-filter="WARRIOR">
                        ⚔️ Warriors
                    </button>
                    <button class="btn ${this.currentFilter === 'SORCERER' ? 'btn-cosmic active' : 'btn-cosmic-outline'}" 
                            data-filter="SORCERER">
                        🔮 Sorcerers
                    </button>
                </div>
            </div>
            <div class="table-responsive">
                <table class="table table-dark table-hover">
                    <thead>
                        <tr>
                            <th scope="col">Rank</th>
                            <th scope="col">Character</th>
                            <th scope="col">Class</th>
                            <th scope="col">Level</th>
                            <th scope="col">Experience</th>
                            <th scope="col">Victories</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${filteredRankings.map((character, index) => {
                            const characterClass = CHARACTER_CLASSES[character.characterClass];
                            const className = characterClass ? characterClass.name : character.characterClass;
                            return `
                                <tr>
                                    <td>
                                        <span class="trophy trophy-${index + 1}">
                                            ${index < 3 ? '🏆' : (index + 1)}
                                        </span>
                                    </td>
                                    <td>${character.name}</td>
                                    <td>
                                        <span class="class-icon" title="${className}">
                                            ${character.characterClass === 'WARRIOR' ? '⚔️' : '🔮'}
                                        </span>
                                    </td>
                                    <td>${formatLevel(character.level)}</td>
                                    <td>${character.experience}</td>
                                    <td>${character.victories}</td>
                                </tr>
                            `;
                        }).join('')}
                    </tbody>
                </table>
            </div>
        `;

        // Add event listeners to filter buttons
        const filterButtons = leaderboardContainer.querySelectorAll('.leaderboard-filters button');
        filterButtons.forEach(button => {
            button.addEventListener('click', () => {
                this.currentFilter = button.dataset.filter;
                this.displayLeaderboard();
            });
        });

        // Initialize tooltips
        const tooltips = leaderboardContainer.querySelectorAll('[title]');
        tooltips.forEach(el => new bootstrap.Tooltip(el));
    }
}

export default LeaderboardTab; 