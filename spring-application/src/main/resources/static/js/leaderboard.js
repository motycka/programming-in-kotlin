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
            : this.rankings.filter(entry => entry.character.characterClass === this.currentFilter);

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
                            <th scope="col">#</th>
                            <th scope="col">Character</th>
                            <th scope="col">Class</th>
                            <th scope="col">Level</th>
                            <th scope="col">Experience</th>
                            <th scope="col">W/L/D</th>
                            <th scope="col">Win Rate</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${filteredRankings.map((entry, index) => {
                            const characterClass = CHARACTER_CLASSES[entry.character.characterClass];
                            const className = characterClass ? characterClass.name : entry.character.characterClass;
                            const winRate = ((entry.victories / (entry.victories + entry.defeats + entry.draws)) * 100).toFixed(1);
                            
                            return `
                                <tr>
                                    <td>
                                        <span class="position-badge ${index < 3 ? 'top-' + (index + 1) : ''}">
                                            ${index < 3 ? '🏆' : (index + 1)}
                                        </span>
                                    </td>
                                    <td>${entry.character.name}</td>
                                    <td>
                                        <span class="class-icon" title="${className}">
                                            ${entry.character.characterClass === 'WARRIOR' ? '⚔️' : '🔮'}
                                        </span>
                                    </td>
                                    <td>${formatLevel(entry.character.level)}</td>
                                    <td>${entry.experience}</td>
                                    <td>
                                        <span class="stats-group">
                                            <span class="stat-win" title="Wins">${entry.victories}</span>
                                            <span class="stat-loss" title="Losses">${entry.defeats}</span>
                                            <span class="stat-draw" title="Draws">${entry.draws}</span>
                                        </span>
                                    </td>
                                    <td>
                                        <div class="win-rate" title="Win Rate">
                                            <div class="progress">
                                                <div class="progress-bar" 
                                                     role="progressbar" 
                                                     style="width: ${winRate}%"
                                                     aria-valuenow="${winRate}" 
                                                     aria-valuemin="0" 
                                                     aria-valuemax="100">
                                                    ${winRate}%
                                                </div>
                                            </div>
                                        </div>
                                    </td>
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