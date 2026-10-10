# Kisan Price – Smart Crop Price Information System (Web Application)

A modern, responsive full-stack web application designed for desktop, laptop, tablet, and mobile browsers that provides farmers with transparent, live market prices of vegetables, fruits, grains, and pulses across Indian APMC mandis.

---

## 🌟 Key Features

1. **Desktop-First Full Web Layout**:
   - Runs directly in standard desktop, laptop, tablet, and mobile browsers.
   - Clean, full-width responsive header with Kisan Price branding, navigation links, multi-lingual language selector, and farmer profile modal.
   - Professional agricultural theme featuring natural harvest greens (`#1b5e20`), warm sun gold (`#d97706`), and crisp card surfaces.

2. **Multilingual Architecture**:
   - Instant language toggling between **English (EN)**, **हिन्दी (HI)**, and **తెలుగు (TE)**.
   - Native crop name and category translations.

3. **Prominent Crop Search & Hierarchical Filtering**:
   - Real-time search with fast commodity suggestions (Tomato, Onion, Wheat, Rice, etc.).
   - Cascading dropdowns: **State → District → Mandi / APMC Market**.
   - Category filtering: **All Crops**, **Vegetables**, **Fruits**, **Grains**, and **Pulses**.
   - Web Speech API voice search button with auto-fallback.
   - Instant **Apply Filters** and **Reset** buttons.

4. **Market Price Presentation**:
   - **Table View**: Comprehensive data table with Min Price, Max Price, Modal/Average Rate, 24h Change, Last Updated, and View Details.
   - **Cards Grid View**: Rich responsive web cards.
   - **Unit Toggle**: 1-click toggle between **Per Kilogram (₹/kg)** and **Per Quintal (₹/qtl)**.
   - Clearly labeled **“Demo Data”** badge for simulated Agmarknet auction spot data.

5. **Interactive Price Trends & Charts**:
   - Interactive line charts powered by Recharts with **7 Days**, **30 Days**, and **90 Days** trajectories.
   - Multi-mandi comparative lines for the same commodity.
   - Automated plain-language trend explanations.

6. **Mandi Comparison Matrix**:
   - Compare the same crop across regional mandis side-by-side.
   - **Highest Paying Mandi Trophy Badge** highlighting maximum profit differences.

7. **Market Locations Directory & About Us**:
   - State-wise and district-wise APMC directory.
   - Agricultural market transparency mission and farmer helpline contact.

---

## 🚀 How to Run the Web Application Locally

### Prerequisites
- Node.js (v18 or higher recommended)
- npm or yarn

### 1. Install Dependencies
```bash
cd web
npm install
```

### 2. Start the Frontend Development Server
```bash
npm run dev
```
Open [http://localhost:3000](http://localhost:3000) in your web browser.

### 3. (Optional) Run the Backend API
In a separate terminal:
```bash
cd web
npm run server
```
The REST API will start at [http://localhost:5000](http://localhost:5000).

---

## 📁 Web Project Structure

```
web/
├── index.html                 # HTML5 entry point
├── package.json               # Dependencies & build scripts
├── vite.config.js             # Vite development server config
├── tailwind.config.js         # Tailwind CSS theme configuration
├── postcss.config.js          # PostCSS plugins
├── server/
│   └── index.js               # Node.js / Express REST API backend
└── src/
    ├── main.jsx               # React DOM entry point
    ├── App.jsx                # Main web application layout
    ├── index.css              # Tailwind base stylesheet
    ├── i18n/
    │   └── translations.js    # English, Hindi & Telugu localization
    ├── data/
    │   └── cropData.js        # Crop dataset, mandis, and historical trends
    └── components/
        ├── Navbar.jsx         # Web header & navigation
        ├── HeroSearch.jsx     # Search bar, filters & voice search
        ├── TopPrices.jsx      # Today's Top Prices carousel/grid
        ├── PriceTable.jsx     # Responsive desktop data table
        ├── PriceCards.jsx     # Responsive web cards grid
        ├── PriceTrendsChart.jsx # Interactive 7d/30d/90d line charts
        ├── MandiComparison.jsx# Mandi comparison table & best rate highlight
        ├── MarketLocations.jsx# State & district APMC directory
        ├── AboutUs.jsx        # About Us & toll-free farmer desk
        ├── FarmerLoginModal.jsx # Farmer login & profile modal
        └── CropDetailModal.jsx # Detailed commodity modal
```
