import type {SidebarsConfig} from '@docusaurus/plugin-content-docs';

// This runs in Node.js - Don't use client-side code here (browser APIs, JSX...)

/**
 * Creating a sidebar enables you to:
 - create an ordered group of docs
 - render a sidebar for each doc of that group
 - provide next/previous navigation

 The sidebars can be generated from the filesystem, or explicitly defined here.

 Create as many sidebars as you want.
 */
const sidebars: SidebarsConfig = {
  tutorialSidebar: [
    'intro',
    {
      type: 'category',
      label: 'Getting Started',
      collapsed: false,
      items: [
        'getting-started/installation',
        'getting-started/build-your-first-app',
        'getting-started/common-patterns',
      ],
    },
    {
      type: 'category',
      label: 'Guides',
      items: [
        'guides/lifecycle',
        'guides/jminiapp',
        'guides/context',
        'guides/runner',
        'guides/config',
        'guides/adapters',
        'guides/custom-adapter',
        'guides/import-strategies',
      ],
    },
    {
      type: 'category',
      label: 'Examples',
      items: [
        'examples/templates',
        'examples/counter',
        'examples/music-wrapped',
      ],
    },
  ],
};

export default sidebars;
