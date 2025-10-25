// Enhanced JavaScript with animations and interactions
document.addEventListener('DOMContentLoaded', function() {
    // Initialize all animations and interactions
    initNavbar();
    initScrollAnimations();
    initPageTransitions();
    initInteractiveElements();
    initLoadingStates();
    initParallaxEffects();
});

// Navbar scroll effect
function initNavbar() {
    const navbar = document.querySelector('.navbar');
    if (navbar) {
        window.addEventListener('scroll', () => {
            if (window.scrollY > 100) {
                navbar.classList.add('scrolled');
            } else {
                navbar.classList.remove('scrolled');
            }
        });
    }

    // Smooth scrolling for navigation links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
}

// Scroll animations for elements
function initScrollAnimations() {
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
                
                // Stagger animation for child elements
                if (entry.target.classList.contains('feature-grid')) {
                    const children = entry.target.children;
                    Array.from(children).forEach((child, index) => {
                        setTimeout(() => {
                            child.classList.add('visible');
                        }, index * 200);
                    });
                }
            }
        });
    }, observerOptions);

    // Observe all elements with fade-in class
    document.querySelectorAll('.fade-in').forEach(el => {
        observer.observe(el);
    });
}

// Page transition animations
function initPageTransitions() {
    // Add loading animation for page transitions
    document.querySelectorAll('a').forEach(link => {
        link.addEventListener('click', (e) => {
            if (link.getAttribute('href') && 
                !link.getAttribute('href').startsWith('#') && 
                !link.getAttribute('href').includes('javascript')) {
                
                const spinner = document.createElement('div');
                spinner.className = 'loading';
                spinner.style.position = 'fixed';
                spinner.style.top = '50%';
                spinner.style.left = '50%';
                spinner.style.transform = 'translate(-50%, -50%)';
                spinner.style.zIndex = '9999';
                document.body.appendChild(spinner);
                
                setTimeout(() => {
                    if (spinner.parentNode) {
                        spinner.parentNode.removeChild(spinner);
                    }
                }, 1000);
            }
        });
    });
}

// Interactive elements
function initInteractiveElements() {
    // Enhanced button interactions
    document.querySelectorAll('.btn').forEach(button => {
        button.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-3px) scale(1.05)';
        });
        
        button.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0) scale(1)';
        });
    });

    // Book card interactions
    document.querySelectorAll('.book-card').forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-10px) scale(1.02)';
        });
        
        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0) scale(1)';
        });
    });

    // Form input animations
    document.querySelectorAll('.form-control').forEach(input => {
        input.addEventListener('focus', function() {
            this.parentElement.classList.add('focused');
        });
        
        input.addEventListener('blur', function() {
            if (!this.value) {
                this.parentElement.classList.remove('focused');
            }
        });
    });

    // Add to cart animations
    document.querySelectorAll('form[action*="/cart/add"]').forEach(form => {
        form.addEventListener('submit', function(e) {
            const button = this.querySelector('button[type="submit"]');
            if (button) {
                const originalText = button.innerHTML;
                button.innerHTML = '<span class="loading"></span> Adding...';
                button.disabled = true;
                
                setTimeout(() => {
                    button.innerHTML = originalText;
                    button.disabled = false;
                    
                    // Show success notification
                    showNotification('Book added to cart successfully!', 'success');
                }, 1500);
            }
        });
    });

    // Wishlist animations
    document.querySelectorAll('form[action*="/wishlist/add"], form[action*="/wishlist/remove"]').forEach(form => {
        form.addEventListener('submit', function(e) {
            const button = this.querySelector('button[type="submit"]');
            if (button) {
                button.style.transform = 'scale(0.9)';
                setTimeout(() => {
                    button.style.transform = 'scale(1)';
                }, 300);
            }
        });
    });
}

// Loading states management
function initLoadingStates() {
    // Global loading handler
    window.addEventListener('beforeunload', function() {
        document.body.style.opacity = '0.7';
        document.body.style.transition = 'opacity 0.3s ease';
    });
}

// Parallax effects
function initParallaxEffects() {
    if (document.querySelector('.hero-section')) {
        window.addEventListener('scroll', () => {
            const scrolled = window.pageYOffset;
            const parallax = document.querySelector('.hero-section');
            if (parallax) {
                parallax.style.transform = `translateY(${scrolled * 0.5}px)`;
            }
        });
    }
}

// Notification system
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `alert alert-${type} position-fixed`;
    notification.style.cssText = `
        top: 20px;
        right: 20px;
        z-index: 9999;
        min-width: 300px;
        animation: slideInRight 0.5s ease-out;
    `;
    notification.innerHTML = `
        ${message}
        <button type="button" class="btn-close btn-close-white ms-2" data-bs-dismiss="alert"></button>
    `;
    
    document.body.appendChild(notification);
    
    // Auto remove after 5 seconds
    setTimeout(() => {
        if (notification.parentNode) {
            notification.style.animation = 'slideOutRight 0.5s ease-out forwards';
            setTimeout(() => {
                if (notification.parentNode) {
                    notification.parentNode.removeChild(notification);
                }
            }, 500);
        }
    }, 5000);
}

// Add slideOutRight animation
const style = document.createElement('style');
style.textContent = `
    @keyframes slideOutRight {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(100%);
            opacity: 0;
        }
    }
`;
document.head.appendChild(style);

// Utility functions
const BookStore = {
    // Format price
    formatPrice: (price) => {
        return new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'USD'
        }).format(price);
    },

    // Debounce function for search
    debounce: (func, wait) => {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    },

    // Animate counter
    animateCounter: (element, target, duration = 2000) => {
        let start = 0;
        const increment = target / (duration / 16);
        const timer = setInterval(() => {
            start += increment;
            if (start >= target) {
                element.textContent = target;
                clearInterval(timer);
            } else {
                element.textContent = Math.floor(start);
            }
        }, 16);
    }
};

// Initialize tooltips
document.addEventListener('DOMContentLoaded', function() {
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
});

// Enhanced search functionality
const searchInput = document.querySelector('input[name="search"]');
if (searchInput) {
    searchInput.addEventListener('input', BookStore.debounce(function(e) {
        const searchTerm = e.target.value;
        if (searchTerm.length > 2) {
            // Add search animation
            e.target.parentElement.classList.add('searching');
            setTimeout(() => {
                e.target.parentElement.classList.remove('searching');
            }, 1000);
        }
    }, 500));
}

// Add CSS for searching state
const searchStyle = document.createElement('style');
searchStyle.textContent = `
    .searching {
        position: relative;
    }
    .searching::after {
        content: '';
        position: absolute;
        right: 10px;
        top: 50%;
        transform: translateY(-50%);
        width: 20px;
        height: 20px;
        border: 2px solid #667eea;
        border-top: 2px solid transparent;
        border-radius: 50%;
        animation: spin 1s linear infinite;
    }
`;
document.head.appendChild(searchStyle);
