<template>
  <div class="about">
    <div class="image-section">
      <img src="@/images/about.png" alt="COCOA connect Logo" class="logo-image" />
    </div>
    <div class="text-section">
      <h3>WEB PROGRAMMING ACADEMIC PROJECT</h3>
      <h1>COCOA CONNECT<br />WEB SERVICE</h1>
      <p><strong>Cocoa Connect</strong> is a web app developed as academic project for course Web Programming at Faculty of Technical Sciences, University of Novi Sad. It is implemented using REST API, Java, JavaScript, HTML and CSS. </p>
      <p> The system is designed to manage chocolate factories and supports 4 user roles, each with specific functionalities and permissions.</p>
      <p><strong>Used Tools: </strong> Microsoft Visual Studio, Eclipse IDE for Enterprise Java and Web Developers</p>
      <p><strong>Authors:</strong> Anja Vujacic, Ana Edelinski</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'About'
}
</script>

<style scoped>
.about {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.image-section {
  flex: 1;
  padding-right: 20px;
}

.image-section img {
  max-width: 100%;
  height: 500px;
}

.text-section {
  flex: 1;
  padding-left: 20px;
  text-align: left;
  padding-right: 90px;
}

.text-section h3 {
  color: #523F31;
  text-transform: uppercase;
}

.text-section h1 {
  font-size: 2.5em;
  margin-bottom: 20px;
  color:#2D1E17;
}

.text-section p {
  margin-bottom: 20px;
  line-height: 1.6;
  text-align: justify;
}
</style>
