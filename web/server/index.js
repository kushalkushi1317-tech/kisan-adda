import express from 'express';
import cors from 'cors';

const app = express();
const PORT = process.env.PORT || 5000;

app.use(cors());
app.use(express.json());

// In-memory data store seeded with official Agmarknet-compatible commodity structures
const categories = [
  "All Crops",
  "Vegetables",
  "Fruits",
  "Grains",
  "Pulses",
  "Spices",
  "Oilseeds",
  "Commercial Crops"
];

const locations = {
  "Karnataka": {
    "Kolar": ["Kolar APMC Market", "Srinivaspur Mandi"],
    "Bengaluru Urban": ["Yeshwanthpur APMC", "K.R. Market"],
    "Kalaburagi": ["Kalaburagi APMC", "Sedam Mandi"],
    "Davanagere": ["Davanagere APMC"]
  },
  "Maharashtra": {
    "Nashik": ["Lasalgaon APMC", "Pimpalgaon Mandi"],
    "Pune": ["Pune Gultekdi Market", "Baramati APMC"],
    "Ratnagiri": ["Ratnagiri Mandi"],
    "Jalgaon": ["Jalgaon APMC"]
  },
  "Andhra Pradesh": {
    "Guntur": ["Guntur Mirchi Yard", "Tenali Mandi"],
    "East Godavari": ["Rajahmundry APMC", "Kakinada Market"],
    "Anantapur": ["Tadipatri Mandi", "Hindupur APMC"]
  },
  "Telangana": {
    "Warangal": ["Warangal Enmamula Mandi"],
    "Khammam": ["Khammam Chilli Yard"],
    "Nizamabad": ["Nizamabad APMC Market"]
  },
  "Uttar Pradesh": {
    "Agra": ["Fatehabad Mandi", "Agra Sadar"],
    "Kanpur": ["Kanpur Grain Mandi"],
    "Varanasi": ["Varanasi APMC"]
  },
  "Madhya Pradesh": {
    "Sehore": ["Sehore Mandi (Sharbati)"],
    "Indore": ["Indore Choithram Mandi"],
    "Mandsaur": ["Mandsaur APMC"]
  },
  "Punjab": {
    "Ludhiana": ["Khanna Grain Market", "Jagraon Mandi"],
    "Jalandhar": ["Jalandhar APMC"]
  },
  "Haryana": {
    "Karnal": ["Karnal Grain Market"],
    "Kurukshetra": ["Thanesar Grain Mandi"]
  }
};

let crops = [
  {
    id: 1,
    name: "Tomato",
    nameHi: "टमाटर",
    nameTe: "టమాట",
    category: "Vegetables",
    icon: "🍅",
    state: "Karnataka",
    district: "Kolar",
    mandi: "Kolar APMC Market",
    minPrice: 28,
    maxPrice: 42,
    modalPrice: 35,
    unitKg: "Rs/Kg",
    priceChange: 12.5,
    lastUpdated: "Today, 10:30 AM",
    grade: "Grade A Hybrid",
    arrivals: "140 Tons",
    isDemoData: true
  },
  {
    id: 2,
    name: "Tomato",
    nameHi: "टमाटर",
    nameTe: "టమాట",
    category: "Vegetables",
    icon: "🍅",
    state: "Karnataka",
    district: "Bengaluru Urban",
    mandi: "Yeshwanthpur APMC",
    minPrice: 25,
    maxPrice: 38,
    modalPrice: 31,
    unitKg: "Rs/Kg",
    priceChange: 8.0,
    lastUpdated: "Today, 11:15 AM",
    grade: "Local Hybrid",
    arrivals: "95 Tons",
    isDemoData: true
  },
  {
    id: 3,
    name: "Onion",
    nameHi: "प्याज",
    nameTe: "ఉల్లిపాయ",
    category: "Vegetables",
    icon: "🧅",
    state: "Maharashtra",
    district: "Nashik",
    mandi: "Lasalgaon APMC",
    minPrice: 24,
    maxPrice: 36,
    modalPrice: 31,
    unitKg: "Rs/Kg",
    priceChange: -4.5,
    lastUpdated: "Today, 09:45 AM",
    grade: "Red Medium",
    arrivals: "320 Tons",
    isDemoData: true
  },
  {
    id: 4,
    name: "Potato",
    nameHi: "आलू",
    nameTe: "బంగాళాదుంప",
    category: "Vegetables",
    icon: "🥔",
    state: "Uttar Pradesh",
    district: "Agra",
    mandi: "Fatehabad Mandi",
    minPrice: 18,
    maxPrice: 26,
    modalPrice: 22.5,
    unitKg: "Rs/Kg",
    priceChange: 5.8,
    lastUpdated: "Today, 10:00 AM",
    grade: "Jyoti Large",
    arrivals: "450 Tons",
    isDemoData: true
  },
  {
    id: 5,
    name: "Wheat",
    nameHi: "गेहूं",
    nameTe: "గోధుమలు",
    category: "Grains",
    icon: "🌾",
    state: "Madhya Pradesh",
    district: "Sehore",
    mandi: "Sehore Mandi (Sharbati)",
    minPrice: 32,
    maxPrice: 46,
    modalPrice: 39,
    unitKg: "Rs/Kg",
    priceChange: 3.2,
    lastUpdated: "Today, 08:30 AM",
    grade: "Sharbati Premium",
    arrivals: "210 Tons",
    isDemoData: true
  },
  {
    id: 6,
    name: "Paddy / Rice",
    nameHi: "धान / चावल",
    nameTe: "వరి / బియ్యం",
    category: "Grains",
    icon: "🌾",
    state: "Andhra Pradesh",
    district: "East Godavari",
    mandi: "Rajahmundry APMC",
    minPrice: 28,
    maxPrice: 42,
    modalPrice: 36,
    unitKg: "Rs/Kg",
    priceChange: 2.1,
    lastUpdated: "Today, 09:15 AM",
    grade: "BPT 5204 (Sona Masoori)",
    arrivals: "180 Tons",
    isDemoData: true
  },
  {
    id: 7,
    name: "Mango (Alphonso)",
    nameHi: "आम (हापुस)",
    nameTe: "మామిడి",
    category: "Fruits",
    icon: "🥭",
    state: "Maharashtra",
    district: "Ratnagiri",
    mandi: "Ratnagiri Mandi",
    minPrice: 95,
    maxPrice: 165,
    modalPrice: 130,
    unitKg: "Rs/Kg",
    priceChange: 8.5,
    lastUpdated: "Today, 11:45 AM",
    grade: "Export Quality GI Tag",
    arrivals: "60 Tons",
    isDemoData: true
  },
  {
    id: 8,
    name: "Banana",
    nameHi: "केला",
    nameTe: "అరటిపండు",
    category: "Fruits",
    icon: "🍌",
    state: "Andhra Pradesh",
    district: "Anantapur",
    mandi: "Tadipatri Mandi",
    minPrice: 20,
    maxPrice: 32,
    modalPrice: 26,
    unitKg: "Rs/Kg",
    priceChange: 4.0,
    lastUpdated: "Today, 09:30 AM",
    grade: "Grand Naine (G9)",
    arrivals: "120 Tons",
    isDemoData: true
  },
  {
    id: 9,
    name: "Red Gram (Toor Dal)",
    nameHi: "अरहर / तुअर दाल",
    nameTe: "కందిపప్పు",
    category: "Pulses",
    icon: "🫘",
    state: "Karnataka",
    district: "Kalaburagi",
    mandi: "Kalaburagi APMC",
    minPrice: 92,
    maxPrice: 125,
    modalPrice: 110,
    unitKg: "Rs/Kg",
    priceChange: 6.8,
    lastUpdated: "Today, 10:15 AM",
    grade: "Red Bold",
    arrivals: "85 Tons",
    isDemoData: true
  },
  {
    id: 10,
    name: "Green Chilli",
    nameHi: "हरी मिर्च",
    nameTe: "పచ్చిమిర్చి",
    category: "Spices",
    icon: "🌶️",
    state: "Andhra Pradesh",
    district: "Guntur",
    mandi: "Guntur Mirchi Yard",
    minPrice: 65,
    maxPrice: 95,
    modalPrice: 82,
    unitKg: "Rs/Kg",
    priceChange: 14.2,
    lastUpdated: "Today, 09:00 AM",
    grade: "Teja Super Hot",
    arrivals: "280 Tons",
    isDemoData: true
  }
];

const importAuditLogs = [
  {
    id: 1,
    adminId: "ADMIN-01",
    sourceInfo: "Government Agmarknet Daily Auction Feed",
    filename: "agmarknet_daily_feed_oct2026.csv",
    successCount: 10,
    failedCount: 0,
    importTimestamp: new Date().toISOString(),
    notes: "Verified auction spot prices with demo format."
  }
];

// 1. Health check
app.get('/api/health', (req, res) => {
  res.json({
    status: 'healthy',
    platform: 'Kissan Adda – Smart Crop Price Information System',
    timestamp: new Date().toISOString()
  });
});

// 2. Categories
app.get('/api/categories', (req, res) => {
  res.json({ success: true, categories });
});

// 3. Location Endpoints
app.get('/api/locations/states', (req, res) => {
  res.json({ success: true, states: Object.keys(locations) });
});

app.get('/api/locations/districts', (req, res) => {
  const { state } = req.query;
  if (!state || !locations[state]) {
    return res.status(400).json({ success: false, message: 'Valid state query parameter required' });
  }
  res.json({ success: true, districts: Object.keys(locations[state]) });
});

app.get('/api/locations/mandis', (req, res) => {
  const { state, district } = req.query;
  if (!state || !district || !locations[state]?.[district]) {
    return res.status(400).json({ success: false, message: 'Valid state and district parameters required' });
  }
  res.json({ success: true, mandis: locations[state][district] });
});

// 4. GET /api/crops
app.get('/api/crops', (req, res) => {
  const { category, state, district, mandi, search, sortBy } = req.query;
  let results = [...crops];

  if (search) {
    const q = search.toLowerCase().trim();
    results = results.filter(c =>
      c.name.toLowerCase().includes(q) ||
      (c.nameHi && c.nameHi.includes(q)) ||
      (c.nameTe && c.nameTe.includes(q)) ||
      c.mandi.toLowerCase().includes(q) ||
      c.district.toLowerCase().includes(q)
    );
  }

  if (category && category !== 'All Crops' && category !== 'All') {
    results = results.filter(c => c.category.toLowerCase() === category.toLowerCase());
  }

  if (state) {
    results = results.filter(c => c.state.toLowerCase() === state.toLowerCase());
  }

  if (district) {
    results = results.filter(c => c.district.toLowerCase() === district.toLowerCase());
  }

  if (mandi) {
    results = results.filter(c => c.mandi.toLowerCase() === mandi.toLowerCase());
  }

  if (sortBy === 'price-desc') {
    results.sort((a, b) => b.modalPrice - a.modalPrice);
  } else if (sortBy === 'price-asc') {
    results.sort((a, b) => a.modalPrice - b.modalPrice);
  } else if (sortBy === 'change-desc') {
    results.sort((a, b) => b.priceChange - a.priceChange);
  } else if (sortBy === 'name') {
    results.sort((a, b) => a.name.localeCompare(b.name));
  }

  res.json({
    success: true,
    count: results.length,
    dataNotice: "Demo Data: Simulated prices, connect official Agmarknet API for live real-time auction feeds.",
    data: results
  });
});

// 5. GET /api/crops/:id
app.get('/api/crops/:id', (req, res) => {
  const id = parseInt(req.params.id, 10);
  const crop = crops.find(c => c.id === id);
  if (!crop) {
    return res.status(404).json({ success: false, message: 'Crop record not found' });
  }
  res.json({ success: true, data: crop });
});

// 6. GET /api/prices/compare
app.get('/api/prices/compare', (req, res) => {
  const { cropName } = req.query;
  if (!cropName) {
    return res.status(400).json({ success: false, message: 'cropName query parameter is required' });
  }
  const matching = crops.filter(c => c.name.toLowerCase() === cropName.toLowerCase());
  matching.sort((a, b) => b.modalPrice - a.modalPrice);

  const highest = matching[0];
  const lowest = matching[matching.length - 1];
  const difference = highest && lowest ? highest.modalPrice - lowest.modalPrice : 0;

  res.json({
    success: true,
    cropName,
    count: matching.length,
    highestPayingMandi: highest?.mandi || null,
    highestPrice: highest?.modalPrice || null,
    priceDifferencePerKg: difference,
    priceDifferencePerQuintal: difference * 100,
    mandis: matching
  });
});

// 7. GET /api/top-prices
app.get('/api/top-prices', (req, res) => {
  const sorted = [...crops].sort((a, b) => b.modalPrice - a.modalPrice).slice(0, 4);
  res.json({ success: true, data: sorted });
});

// 8. POST /api/contact
app.post('/api/contact', (req, res) => {
  const { name, phone, email, subject, message } = req.body;
  if (!name || (!phone && !email) || !message) {
    return res.status(400).json({ success: false, message: 'Please provide name, contact detail, and message' });
  }

  res.json({
    success: true,
    message: 'Thank you! Your inquiry has been submitted to the Kissan Adda agricultural support team.',
    ticketId: `KA-${Math.floor(100000 + Math.random() * 900000)}`
  });
});

// 9. Admin CSV Import
app.post('/api/admin/import-csv', (req, res) => {
  const { records, filename } = req.body;
  if (!Array.isArray(records) || records.length === 0) {
    return res.status(400).json({ success: false, message: 'Invalid or empty CSV records payload' });
  }

  let successCount = 0;
  let failedCount = 0;

  records.forEach((row, idx) => {
    if (row.name && row.mandi && row.modalPrice) {
      crops.push({
        id: crops.length + 1,
        name: row.name,
        category: row.category || "Vegetables",
        icon: row.icon || "🌾",
        state: row.state || "Karnataka",
        district: row.district || "Kolar",
        mandi: row.mandi,
        minPrice: parseFloat(row.minPrice) || parseFloat(row.modalPrice) * 0.85,
        maxPrice: parseFloat(row.maxPrice) || parseFloat(row.modalPrice) * 1.15,
        modalPrice: parseFloat(row.modalPrice),
        unitKg: "Rs/Kg",
        priceChange: 0.0,
        lastUpdated: "Imported via CSV",
        grade: row.grade || "Standard",
        arrivals: "N/A",
        isDemoData: true
      });
      successCount++;
    } else {
      failedCount++;
    }
  });

  const audit = {
    id: importAuditLogs.length + 1,
    adminId: "ADMIN-SESSION",
    sourceInfo: "CSV Upload",
    filename: filename || "uploaded_data.csv",
    successCount,
    failedCount,
    importTimestamp: new Date().toISOString(),
    notes: `Imported ${successCount} records successfully.`
  };
  importAuditLogs.push(audit);

  res.json({
    success: true,
    message: `CSV import completed: ${successCount} successful, ${failedCount} failed.`,
    audit
  });
});

// 10. GET /api/admin/audit-logs
app.get('/api/admin/audit-logs', (req, res) => {
  res.json({ success: true, logs: importAuditLogs });
});

app.listen(PORT, () => {
  console.log(`Kissan Adda API server running on port ${PORT}`);
});
