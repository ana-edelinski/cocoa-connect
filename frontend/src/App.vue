<template>
  <div id="app">
    <nav class="navbar">
      <div class="logo">
        <img src="@/images/logo-01.png" alt="COCOA connect Logo" class="logo-image" />
      </div>
      <div class="nav-links">
        <router-link to="/" class="nav-link">Home</router-link>
        <router-link to="/addChocolate" class="nav-link">Add Chocolate</router-link>
        <router-link to="/login" class="nav-link">Login</router-link>
      </div>
    </nav>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App'
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
</style>
