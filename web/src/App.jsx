import React, { useState, useRef, useMemo } from 'react';
import Navbar from './components/Navbar';
import HeroSearch from './components/HeroSearch';
import TopPrices from './components/TopPrices';
import PriceTable from './components/PriceTable';
import PriceCards from './components/PriceCards';
import PriceTrendsChart from './components/PriceTrendsChart';
import MandiComparison from './components/MandiComparison';
import MarketLocations from './components/MarketLocations';
import AboutUs from './components/AboutUs';
import FarmerLoginModal from './components/FarmerLoginModal';
import CropDetailModal from './components/CropDetailModal';
import { initialCrops } from './data/cropData';
import { translations } from './i18n/translations';
import { Table, LayoutGrid, ArrowDownUp, RefreshCw, Sprout } from 'lucide-react';

export default function App() {
  const [currentLang, setCurrentLang] = useState('en');
  const [activeSection, setActiveSection] = useState('home');
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('All');
  const [selectedState, setSelectedState] = useState('');
  const [selectedDistrict, setSelectedDistrict] = useState('');
  const [selectedMandi, setSelectedMandi] = useState('');
  const [unit, setUnit] = useState('kg'); // 'kg' or 'quintal'
  const [viewMode, setViewMode] = useState('table'); // 'table' or 'cards'
  const [sortBy, setSortBy] = useState('price-desc');

  const [isLoginOpen, setIsLoginOpen] = useState(false);
  const [selectedCropDetail, setSelectedCropDetail] = useState(null);

  const searchInputRef = useRef(null);
  const t = translations[currentLang] || translations.en;

  // Filter crops based on search & selectors
  const filteredCrops = useMemo(() => {
    return initialCrops.filter((crop) => {
      // Search term filter across English, Hindi, and Telugu names
      if (searchTerm.trim()) {
        const q = searchTerm.toLowerCase().trim();
        const matchesName = crop.name.toLowerCase().includes(q);
        const matchesHi = crop.nameHi && crop.nameHi.includes(q);
        const matchesTe = crop.nameTe && crop.nameTe.includes(q);
        const matchesMandi = crop.mandi.toLowerCase().includes(q);
        const matchesDist = crop.district.toLowerCase().includes(q);
        const matchesState = crop.state.toLowerCase().includes(q);
        if (!matchesName && !matchesHi && !matchesTe && !matchesMandi && !matchesDist && !matchesState) {
          return false;
        }
      }

      // Category filter
      if (selectedCategory !== 'All' && crop.category !== selectedCategory) {
        return false;
      }

      // State filter
      if (selectedState && crop.state !== selectedState) {
        return false;
      }

      // District filter
      if (selectedDistrict && crop.district !== selectedDistrict) {
        return false;
      }

      // Mandi filter
      if (selectedMandi && crop.mandi !== selectedMandi) {
        return false;
      }

      return true;
    }).sort((a, b) => {
      if (sortBy === 'price-desc') return b.modalPrice - a.modalPrice;
      if (sortBy === 'price-asc') return a.modalPrice - b.modalPrice;
      if (sortBy === 'change-desc') return b.priceChange - a.priceChange;
      if (sortBy === 'name') return a.name.localeCompare(b.name);
      return 0;
    });
  }, [searchTerm, selectedCategory, selectedState, selectedDistrict, selectedMandi, sortBy]);

  const handleResetFilters = () => {
    setSearchTerm('');
    setSelectedCategory('All');
    setSelectedState('');
    setSelectedDistrict('');
    setSelectedMandi('');
  };

  const handleNavigate = (sectionId) => {
    setActiveSection(sectionId);
    if (sectionId === 'prices') {
      const el = document.getElementById('market-prices-section');
      if (el) el.scrollIntoView({ behavior: 'smooth' });
    } else if (sectionId === 'trends') {
      const el = document.getElementById('trends-section');
      if (el) el.scrollIntoView({ behavior: 'smooth' });
    } else if (sectionId === 'locations') {
      const el = document.getElementById('locations-section');
      if (el) el.scrollIntoView({ behavior: 'smooth' });
    } else if (sectionId === 'about') {
      const el = document.getElementById('about-section');
      if (el) el.scrollIntoView({ behavior: 'smooth' });
    } else {
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  };

  const handleFilterMandiFromDirectory = (state, district, mandi) => {
    setSelectedState(state);
    setSelectedDistrict(district);
    setSelectedMandi(mandi);
    const el = document.getElementById('market-prices-section');
    if (el) el.scrollIntoView({ behavior: 'smooth' });
  };

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col selection:bg-harvest-200 selection:text-harvest-900">
      
      {/* 1. Desktop Website Header */}
      <Navbar
        currentLang={currentLang}
        onLangChange={setCurrentLang}
        activeSection={activeSection}
        onNavigate={handleNavigate}
        onOpenLogin={() => setIsLoginOpen(true)}
        onSearchFocus={() => {
          searchInputRef.current?.focus();
          window.scrollTo({ top: 0, behavior: 'smooth' });
        }}
      />

      {/* 2. Hero Search & Filtering Section */}
      <HeroSearch
        currentLang={currentLang}
        searchTerm={searchTerm}
        onSearchChange={setSearchTerm}
        selectedCategory={selectedCategory}
        onCategoryChange={setSelectedCategory}
        selectedState={selectedState}
        onStateChange={setSelectedState}
        selectedDistrict={selectedDistrict}
        onDistrictChange={setSelectedDistrict}
        selectedMandi={selectedMandi}
        onMandiChange={setSelectedMandi}
        onApplyFilters={() => {
          const el = document.getElementById('market-prices-section');
          if (el) el.scrollIntoView({ behavior: 'smooth' });
        }}
        onResetFilters={handleResetFilters}
        searchInputRef={searchInputRef}
      />

      {/* 3. Today's Top Prices Section */}
      <TopPrices
        currentLang={currentLang}
        crops={initialCrops}
        unit={unit}
        onSelectCrop={(c) => setSelectedCropDetail(c)}
      />

      {/* 4. Current Market Prices List / Table Section */}
      <section id="market-prices-section" className="py-12 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 w-full">
        {/* Section Controls Toolbar */}
        <div className="flex flex-col lg:flex-row lg:items-center justify-between gap-4 mb-6">
          <div>
            <h2 className="text-2xl sm:text-3xl font-black text-gray-900 tracking-tight">
              {t.marketPricesTitle}
            </h2>
            <p className="text-sm text-gray-600 font-medium mt-1">
              {t.marketPricesSubtitle} ({filteredCrops.length} reporting mandis found)
            </p>
          </div>

          <div className="flex flex-wrap items-center gap-3">
            {/* Unit Switcher: Per Kg vs Per Quintal */}
            <div className="inline-flex items-center bg-white border border-gray-200 rounded-xl p-1 shadow-sm">
              <button
                onClick={() => setUnit('kg')}
                className={`px-3 py-1.5 rounded-lg text-xs font-bold transition-all ${
                  unit === 'kg'
                    ? 'bg-harvest-700 text-white shadow-sm'
                    : 'text-gray-600 hover:text-gray-900'
                }`}
              >
                ₹/kg (Per Kg)
              </button>
              <button
                onClick={() => setUnit('quintal')}
                className={`px-3 py-1.5 rounded-lg text-xs font-bold transition-all ${
                  unit === 'quintal'
                    ? 'bg-harvest-700 text-white shadow-sm'
                    : 'text-gray-600 hover:text-gray-900'
                }`}
              >
                ₹/qtl (Per Quintal)
              </button>
            </div>

            {/* Sort Dropdown */}
            <select
              value={sortBy}
              onChange={(e) => setSortBy(e.target.value)}
              className="bg-white border border-gray-200 rounded-xl px-3 py-2 text-xs font-bold text-gray-700 shadow-sm focus:outline-none focus:ring-2 focus:ring-harvest-600 cursor-pointer"
            >
              <option value="price-desc">Highest Price First</option>
              <option value="price-asc">Lowest Price First</option>
              <option value="change-desc">Top Gainers (24h)</option>
              <option value="name">Commodity Name (A-Z)</option>
            </select>

            {/* View Mode Toggle (Table vs Cards) */}
            <div className="inline-flex items-center bg-white border border-gray-200 rounded-xl p-1 shadow-sm">
              <button
                onClick={() => setViewMode('table')}
                className={`p-1.5 rounded-lg transition-colors ${
                  viewMode === 'table' ? 'bg-gray-100 text-harvest-800' : 'text-gray-400 hover:text-gray-700'
                }`}
                title={t.viewTable}
              >
                <Table className="w-4 h-4" />
              </button>
              <button
                onClick={() => setViewMode('cards')}
                className={`p-1.5 rounded-lg transition-colors ${
                  viewMode === 'cards' ? 'bg-gray-100 text-harvest-800' : 'text-gray-400 hover:text-gray-700'
                }`}
                title={t.viewCards}
              >
                <LayoutGrid className="w-4 h-4" />
              </button>
            </div>
          </div>
        </div>

        {/* Render Table View or Cards View */}
        {viewMode === 'table' ? (
          <PriceTable
            currentLang={currentLang}
            crops={filteredCrops}
            unit={unit}
            onSelectCrop={(c) => setSelectedCropDetail(c)}
          />
        ) : (
          <PriceCards
            currentLang={currentLang}
            crops={filteredCrops}
            unit={unit}
            onSelectCrop={(c) => setSelectedCropDetail(c)}
          />
        )}
      </section>

      {/* 5. Price Trends & Charts Section */}
      <PriceTrendsChart
        currentLang={currentLang}
        crops={initialCrops}
        unit={unit}
      />

      {/* 6. Compare Markets Section */}
      <MandiComparison
        currentLang={currentLang}
        crops={initialCrops}
        unit={unit}
        onSelectCrop={(c) => setSelectedCropDetail(c)}
      />

      {/* 7. Market Locations Directory */}
      <MarketLocations
        onFilterMandi={handleFilterMandiFromDirectory}
      />

      {/* 8. About Us & Mission */}
      <AboutUs
        currentLang={currentLang}
      />

      {/* Footer */}
      <footer className="bg-gray-900 text-gray-400 py-12 border-t border-gray-800 mt-auto">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex flex-col md:flex-row items-center justify-between gap-6 pb-8 border-b border-gray-800">
            <div className="flex items-center space-x-3">
              <div className="w-10 h-10 rounded-xl bg-harvest-700 flex items-center justify-center text-white">
                <Sprout className="w-6 h-6" />
              </div>
              <div>
                <span className="text-xl font-black text-white">{t.brand}</span>
                <p className="text-xs text-gray-400">{t.tagline}</p>
              </div>
            </div>

            <div className="flex flex-wrap items-center gap-6 text-sm font-semibold">
              <button onClick={() => handleNavigate('home')} className="hover:text-white transition-colors">{t.home}</button>
              <button onClick={() => handleNavigate('prices')} className="hover:text-white transition-colors">{t.cropPrices}</button>
              <button onClick={() => handleNavigate('locations')} className="hover:text-white transition-colors">{t.marketLocations}</button>
              <button onClick={() => handleNavigate('trends')} className="hover:text-white transition-colors">{t.priceTrends}</button>
              <button onClick={() => handleNavigate('about')} className="hover:text-white transition-colors">{t.aboutUs}</button>
            </div>
          </div>

          <div className="pt-8 flex flex-col sm:flex-row items-center justify-between gap-4 text-xs text-gray-500">
            <p>{t.footerText}</p>
            <p>Data Format: Simulated Agmarknet Spot Auction Format</p>
          </div>
        </div>
      </footer>

      {/* Modals */}
      <FarmerLoginModal
        isOpen={isLoginOpen}
        onClose={() => setIsLoginOpen(false)}
        currentLang={currentLang}
      />

      <CropDetailModal
        crop={selectedCropDetail}
        isOpen={Boolean(selectedCropDetail)}
        onClose={() => setSelectedCropDetail(null)}
        currentLang={currentLang}
        unit={unit}
      />

    </div>
  );
}
