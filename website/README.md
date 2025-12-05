# Website

This website is built using [Docusaurus](https://docusaurus.io/), a modern static website generator.

## Prerequisites

### Node.js Installation

**We strongly recommend using NVM (Node Version Manager)** to manage your Node.js installation. NVM allows you to easily switch between Node.js versions and avoid permission issues.

#### Install NVM

**macOS/Linux:**
```bash
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash
```

Or using wget:
```bash
wget -qO- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash
```

**Windows:**

Download and install [nvm-windows](https://github.com/coreybutler/nvm-windows/releases)

#### Install Node.js using NVM

After installing NVM, restart your terminal and run:
```bash
nvm install --lts
nvm use --lts
```

This will install and use the latest Long Term Support (LTS) version of Node.js.

#### Verify Installation

```bash
node --version
npm --version
```

#### Alternative: Direct Node.js Installation

If you prefer not to use NVM, you can install Node.js directly from [nodejs.org](https://nodejs.org/). However, we still recommend NVM for better version management.

## Run

```bash
cd website
npm install
npm start
```

Website will be running at http://localhost:3000/