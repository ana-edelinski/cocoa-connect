<template>
  <div id="app">
    <nav class="navbar">
      <div class="logo">
        <img src="@/images/logo-01.png" alt="COCOA connect Logo" class="logo-image" />
      </div>
      <div class="nav-links">
        <router-link to="/" class="nav-link">Home</router-link>
        <router-link to="/add-chocolate" class="nav-link">Add Chocolate</router-link>
        <router-link to="/create-factory" class="nav-link">Create Factory</router-link>
        <router-link to="/signIn" class="nav-link">Sign In</router-link>
        <router-link to="/registration" class="nav-link">Sign Up</router-link>
       

        <div class="dropdown">
          <img src="@/images/logout.png" alt="User Icon" class="user-icon" @click="toggleDropdown" />
          <div v-if="showDropdown" class="dropdown-menu">
            <router-link to="/my-account" class="dropdown-item" @click.native="closeDropdown">My Account</router-link>
            <router-link to="/log-out" class="dropdown-item" @click.native="closeDropdown">Log Out</router-link>
          </div>
        </div>
      </div>
    </nav>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      showDropdown: false
    }
  },
  mounted() {
    document.addEventListener('click', this.handleClickOutside);
  },
  beforeDestroy() {
    document.removeEventListener('click', this.handleClickOutside);
  },
  methods: {
    toggleDropdown() {
      this.showDropdown = !this.showDropdown;
    },
    closeDropdown() {
      this.showDropdown = false;
    },
    handleClickOutside(event) {
      if (!this.$el.contains(event.target)) {
        this.closeDropdown();
      }
    }
  }
}
</script>

<style>
#app {
  font-family: "Poppins", sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 40px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-bottom: 1px solid #2D1E17; 
}

.logo {
  flex: 1;
}

.logo-image {
  height: 70px; 
}

.nav-links {
  display: flex;
  gap: 20px;
  flex: 2;
  justify-content: center; 
}

.nav-link {
  font-weight: bold;
  color: #2D1E17;
  text-decoration: none;
  padding: 5px 10px;
}

.nav-link.router-link-exact-active {
  color: #796254; 
}

.dropdown {
  position: relative;
  display: inline-block;
}

.user-icon {
  height: 27px;
  cursor: pointer;
}

.dropdown-menu {
  display: block;
  position: absolute;
  top: 40px;
  right: 0;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border: 1px solid #ddd;
  z-index: 1000;
}

.dropdown-item {
  display: block;
  padding: 10px 20px;
  text-decoration: none;
  color: #2D1E17;
  white-space: nowrap;
}

.dropdown-item:hover {
  background-color: #f1f1f1;
}
</style>
