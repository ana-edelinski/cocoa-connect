<template>
  <div id="app">
    <nav class="navbar">
      <div class="nav-links-left">
        <div v-if="loggedUserRole === 'ADMINISTRATOR'">
          <router-link to="/" class="nav-link">HOME</router-link>
          <router-link to="/registered-users" class="nav-link">REGISTERED USERS</router-link>
          <router-link to="/create-factory" class="nav-link">CREATE FACTORY</router-link>
        </div>
        <div v-else-if="loggedUserRole === 'CUSTOMER'">
          <router-link to="/" class="nav-link">HOME</router-link>
        </div>
        <div v-else-if="loggedUserRole === 'MANAGER'">
          <router-link to="/" class="nav-link">HOME</router-link>
          <router-link to="/add-chocolate" class="nav-link">ADD CHOCOLATE</router-link>
          <router-link to="/add-employee" class="nav-link">ADD EMPLOYEE</router-link>
        </div>
        <div v-else-if="loggedUserRole === 'EMPLOYEE'">
          <router-link to="/" class="nav-link">HOME</router-link>
        </div>
        <div v-else>
          <router-link to="/" class="nav-link">HOME</router-link>
        </div>
      </div>
      <div class="logo" @click="refreshHomePage">
        <img src="@/images/logo-01.png" alt="COCOA connect Logo" class="logo-image" />
      </div>
      <div class="nav-links-right">
        <div v-if="!loggedUserRole">
          <router-link to="/signIn" class="nav-link">LOGIN / REGISTER</router-link>
        </div>
        <div class="dropdown" v-if="loggedUserRole">
          <img src="@/images/logout.png" alt="User Icon" class="user-icon" @click="toggleDropdown" />
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
export default {
  name: 'App',
  data() {
    return {
      showDropdown: false,
      loggedUserRole: null,
    }
  },
  mounted() {
    this.initialize();
    document.addEventListener('click', this.handleClickOutside);
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
      this.showDropdown = !this.showDropdown;
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
    refreshHomePage() {
      this.$router.push('/'); 
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

}

.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 40px;
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
}

.nav-links-left {
  flex: 1;
  justify-content: flex-start;
  gap: 20px;
}

.nav-links-right {
  flex: 1;
  justify-content: flex-end;
  gap: 20px;
}

.logo {
  flex: 1;
  display: flex;
  justify-content: center;
  cursor: pointer; 
}

.logo-image {
  height: 70px;
}

.nav-link {
  font-weight:500; 
  font-size: medium;
  color: #2D1E17;
  text-decoration: none;
  padding: 5px 10px;
  text-transform: uppercase;
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
  margin-right: 100px;
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
  margin-right: 50px;
}

.dropdown-item {
  display: block;
  padding: 10px 20px;
  text-decoration: none;
  color: #2D1E17;
  white-space: nowrap;
  text-transform: uppercase; 
}

.dropdown-item:hover {
  background-color: #f1f1f1;
}
</style>
