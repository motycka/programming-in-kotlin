import { fetchWithAuth, CHARACTER_CLASSES } from '../script.js';
import { formatLevel } from './characters.js';

class LeaderboardTab {
    constructor() {
        this.initialize();
    }

    initialize() {
        console.log('Initializing leaderboard tab...');
        
        // Add event listener for tab show
        document.getElementById('leaderboard-tab').addEventListener('shown.bs.tab', () => {
            console.log('Leaderboard tab shown, loading leaderboard...');
            this.loadLeaderboard();
        });
    }

    async loadLeaderboard() {
        console.log('Loading leaderboard...');
        const container = document.getElementById('leaderboardContent');
        try {
            container.innerHTML = '<p class="text-muted text-center">Loading leaderboard...</p>';
            
            const leaderboard = await fetchWithAuth('/api/scores');
            this.displayLeaderboard(leaderboard);
        } catch (error) {
            console.error('Error loading leaderboard:', error);
            container.innerHTML = `
                <div class="empty-state">
                    <i class="fas fa-exclamation-circle fa-3x"></i>
                    <p>Failed to load leaderboard</p>
                    <small>${error.message}</small>
                </div>
            `;
            showToast(error.message, true);
        }
    }

    displayLeaderboard(leaderboard) {
        const container = document.getElementById('leaderboardContent');
        
        if (!leaderboard || leaderboard.length === 0) {
            container.innerHTML = `
                <div class="empty-state">
                    <i class="fas fa-trophy fa-3x"></i>
                    <p>No rankings yet</p>
                    <small>Complete matches to see rankings</small>
                </div>
            `;
            return;
        }

        container.innerHTML = `
            <div class="table-responsive">
                <table class="table leaderboard-table">
                    <thead>
                        <tr>
                            <th>Rank</th>
                            <th>Character</th>
                            <th>Class</th>
                            <th>Level</th>
                            <th>W/L/D</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${leaderboard.map(entry => `
                            <tr>
                                <td class="position">
                                    ${this.formatPosition(entry.position)}
                                </td>
                                <td>${entry.character.name}</td>
                                <td>${CHARACTER_CLASSES[entry.character.characterClass].name}</td>
                                <td>${formatLevel(entry.character.level)}</td>
                                <td>${entry.wins}/${entry.losses}/${entry.draws}</td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            </div>
        `;
    }

    formatPosition(position) {
        if (position <= 3) {
            return `<i class="fas fa-trophy trophy-${position}"></i>`;
        }
        return position;
    }
}

export default LeaderboardTab; 