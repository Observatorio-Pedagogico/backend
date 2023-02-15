const { defineConfig } = require('cypress')

module.exports = defineConfig({
  reporter: 'cypress-multi-reporters',
  reporterOptions: {
    configFile: 'reporter-config.json',
  },
  e2e: {
    setupNodeEvents(on, config) {},
    baseUrl: 'http://localhost:8080/observatorio-pedagogico/api',
  },
  compilerOptions: {
    types: ["cypress"]
  }
})
