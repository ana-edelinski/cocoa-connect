<template>
  <div id="app">
    <nav class="navbar">
      <div class="nav-links-left">
        <router-link to="/" class="nav-link" exact>HOME</router-link>
        <router-link to="/about" class="nav-link">ABOUT US</router-link>
      </div>
      <div class="logo" :class="{ 'scrolled': isScrolled }" @click="refreshHomePage">
        <img src="@/images/logo-01.png" alt="COCOA connect Logo" class="logo-image" />
      </div>
      <div class="nav-links-right">
        <div v-if="loggedUserRole === 'ADMINISTRATOR'">
          <router-link to="/registered-users" class="nav-link">REGISTERED USERS</router-link>
          <router-link to="/create-factory" class="nav-link">CREATE FACTORY</router-link>
        </div>
        <div v-else-if="loggedUserRole === 'MANAGER'">
          <router-link to="/my-factory" class="nav-link" @click="fetchFactoryAndNavigate">MY FACTORY</router-link>
          <router-link to="/add-chocolate" class="nav-link">ADD CHOCOLATE</router-link>
          <router-link to="/add-employee" class="nav-link">ADD EMPLOYEE</router-link>
        </div>
        <div v-else-if="loggedUserRole === 'CUSTOMER'">
          <router-link to="/cart" class="nav-link cart-link">
            <img src="@/images/shopping-cart.png" alt="Shopping Cart" class="shopping-cart-icon" />
          </router-link>
        </div>
        <div v-if="!loggedUserRole">
          <router-link to="/signIn" class="nav-link">LOGIN / REGISTER</router-link>
        </div>
        <div class="dropdown" v-if="loggedUserRole" @mouseleave="closeDropdown">
          <img src="@/images/logout.png" alt="User Icon" class="user-icon" @mouseover="toggleDropdown" />
          <div v-if="showDropdown" class="dropdown-menu">
            <router-link to="/my-account" class="dropdown-item" @click.native="closeDropdown">MY ACCOUNT</router-link>
            <router-link to="/" class="dropdown-item" @click.native="logOut">LOG OUT</router-link>
          </div>
        </div>
      </div>
    </nav>
    <router-view />
    <footer class="footer">
      <p>&copy; Cocoa Connect 2024</p>
    </footer>
  </div>
</template>

<script>
import { ref } from 'vue';
import axios from 'axios';

export default {
  name: 'App',
  data() {
    return {
      showDropdown: false,
      loggedUserRole: null,
      isScrolled: false,
    }
  },
  mounted() {
    this.initialize();
    document.addEventListener('click', this.handleClickOutside);
    window.addEventListener('scroll', this.handleScroll);
  },
  watch: {
    '$route'() {
      this.initialize();
    }
  },
  beforeDestroy() {
    document.removeEventListener('click', this.handleClickOutside);
  },
  methods: {
    initialize() {
      let loggedUsrStr = localStorage.getItem('loggedUser');
      if (loggedUsrStr !== null) {
        let loggedUser = JSON.parse(loggedUsrStr);
        this.loggedUserRole = loggedUser.role;
      } else {
        this.loggedUserRole = null;
      }
    },
    toggleDropdown() {
      this.showDropdown = true;
    },
    closeDropdown() {
      this.showDropdown = false;
    },
    logOut() {
      localStorage.clear();
      this.loggedUserRole = null;
      this.closeDropdown();
      this.$router.push('/');
    },
    handleClickOutside(event) {
      if (!this.$el.contains(event.target)) {
        this.closeDropdown();
      }
    },
    handleScroll() {
      this.isScrolled = window.scrollY > 0;
    },
    refreshHomePage() {
      this.$router.push('/');
    },
    async fetchFactoryAndNavigate() {
      try {
        let loggedUsrStr = localStorage.getItem('loggedUser');
        if (loggedUsrStr !== null) {
          let loggedUser = JSON.parse(loggedUsrStr);
          let managerId = loggedUser.id; 

          if (!managerId) {
            throw new Error('Manager ID is not defined');
          }

          let response = await axios.get(`http://localhost:8080/chocolate-factory/rest/factories/manager/${managerId}`);
          
          if (!response.data || !response.data.id) {
            throw new Error('Factory data not found or does not contain ID');
          }

          let factoryId = response.data.id;
          this.$router.push(`/chocolates/${factoryId}`);
        } else {
          throw new Error('Logged user not found');
        }
      } catch (error) {
        console.error(error);
        alert('An error occurred while fetching factory data');
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
  padding-top: 50px; 
  margin-left: -4px;
}

.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px; 
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-bottom: 1px solid #2D1E17;
  position: fixed; 
  top: 0; 
  width: 100%; 
  background-color: #fff; 
  z-index: 1000; 
}

.nav-links-left, .nav-links-right {
  display: flex;
  align-items: center;
  margin-right: 100px;
  margin-left: 45px;
}

.nav-links-left {
  flex: 1;
  justify-content: flex-start;
}

.nav-links-right {
  flex: 1;
  justify-content: flex-end;
}

.logo {
  flex: 1;
  display: flex;
  justify-content: center;
  cursor: pointer; 
  transition: height 0.3s ease;
}

.logo-image {
  height: 60px; 
  transition: height 0.3s ease;
}

.scrolled .logo-image {
  height: 40px; 
}

.nav-link {
  font-weight: 500; 
  font-size: small;
  color: #2D1E17;
  text-decoration: none;
  padding: 5px 10px;
  text-transform: uppercase;
  margin: 0 5px;
}

.nav-link.router-link-active {
  border-bottom: 2px solid #796254; 
}

.nav-link:hover {
  border-bottom: 2px solid #796254; 
}

.dropdown {
  position: relative;
  display: inline-block;
}

.user-icon {
  height: 23px;
  cursor: pointer;
  margin-left: 10px;
}

.dropdown-menu {
  display: block;
  position: absolute;
  right: -10px; 
  top: 30px; 
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
  text-transform: uppercase; 
  font-size: smaller;
}

.dropdown-item:hover {
  background-color: #f1f1f1;
}

.shopping-cart-icon {
  height: 30px; 
  margin-left: -5px;
}
</style>
