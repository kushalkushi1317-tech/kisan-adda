import React, { useState } from 'react';
import { Search, Mic, RotateCcw, Filter, MapPin, Sparkles, AlertCircle } from 'lucide-react';
import { translations } from '../i18n/translations';
import { statesAndDistricts } from '../data/cropData';

export default function HeroSearch({
  currentLang,
  searchTerm,
  onSearchChange,
  selectedCategory,
  onCategoryChange,
  selectedState,
  onStateChange,
  selectedDistrict,
  onDistrictChange,
  selectedMandi,
  onMandiChange,
  onApplyFilters,
  onResetFilters,
  searchInputRef
}) {
  const t = translations[currentLang] || translations.en;
  const [isListening, setIsListening] = useState(false);
  const [voiceNotice, setVoiceNotice] = useState('');

  const categories = [
    { id: 'All', label: t.allCrops, icon: '🌾' },
    { id: 'Vegetables', label: t.vegetables, icon: '🥦' },
    { id: 'Fruits', label: t.fruits, icon: '🍎' },
    { id: 'Grains', label: t.grains, icon: '🌾' },
    { id: 'Pulses', label: t.pulses, icon: '🫘' }
  ];

  const cropSuggestions = ['Tomato', 'Onion', 'Potato', 'Wheat', 'Paddy / Rice', 'Mango', 'Banana', 'Green Chilli', 'Red Gram'];

  const availableDistricts = selectedState && statesAndDistricts[selectedState]
    ? Object.keys(statesAndDistricts[selectedState])
    : [];

  const availableMandis = selectedState && selectedDistrict && statesAndDistricts[selectedState]?.[selectedDistrict]
    ? statesAndDistricts[selectedState][selectedDistrict]
    : [];

  const handleVoiceSearch = () => {
    const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
    if (!SpeechRecognition) {
      setVoiceNotice('Voice search not supported in this browser. Please use keyboard.');
      setTimeout(() => setVoiceNotice(''), 4000);
      return;
    }

    try {
      const recognition = new SpeechRecognition();
      recognition.lang = currentLang === 'hi' ? 'hi-IN' : currentLang === 'te' ? 'te-IN' : 'en-IN';
      recognition.interimResults = false;
      recognition.maxAlternatives = 1;

      setIsListening(true);
      setVoiceNotice('Listening... Speak crop name now');

      recognition.onresult = (event) => {
        const spoken = event.results[0][0].transcript;
        onSearchChange(spoken);
        setVoiceNotice(`Heard: "${spoken}"`);
        setIsListening(false);
        setTimeout(() => setVoiceNotice(''), 3000);
      };

      recognition.onerror = () => {
        setIsListening(false);
        setVoiceNotice('Could not hear voice input. Please type.');
        setTimeout(() => setVoiceNotice(''), 3000);
      };

      recognition.onend = () => {
        setIsListening(false);
      };

      recognition.start();
    } catch {
      setIsListening(false);
      setVoiceNotice('Voice search error. Please type query.');
      setTimeout(() => setVoiceNotice(''), 3000);
    }
  };

  return (
    <div className="bg-gradient-to-b from-harvest-50 via-white to-white border-b border-gray-200 py-10 lg:py-14">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        
        {/* Notice Banner */}
        <div className="mb-6 inline-flex items-center space-x-2 bg-amber-50 border border-amber-200 text-amber-900 px-4 py-2 rounded-xl text-xs sm:text-sm font-semibold shadow-sm">
          <AlertCircle className="w-4 h-4 text-amber-700 flex-shrink-0" />
          <span>{t.demoDataNotice}</span>
        </div>

        {/* Hero Headlines */}
        <div className="max-w-3xl mb-8">
          <h1 className="text-3xl sm:text-4xl lg:text-5xl font-black text-gray-900 tracking-tight leading-tight">
            Smart Crop Price <span className="text-harvest-700">Information System</span>
          </h1>
          <p className="mt-3 text-lg text-gray-600 font-medium">
            {t.tagline} Compare verified APMC spot prices across India and maximize farm profits.
          </p>
        </div>

        {/* Search Box & Filters Container */}
        <div className="bg-white rounded-2xl shadow-xl border border-gray-200/80 p-5 lg:p-7">
          
          {/* Main Search Input */}
          <div className="relative mb-5">
            <div className="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
              <Search className="h-5 w-5 text-gray-400" />
            </div>
            <input
              ref={searchInputRef}
              type="text"
              value={searchTerm}
              onChange={(e) => onSearchChange(e.target.value)}
              placeholder={t.searchPlaceholder}
              className="block w-full pl-12 pr-28 py-4 bg-gray-50 hover:bg-gray-100/50 focus:bg-white border border-gray-300 rounded-xl text-base text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-harvest-600 focus:border-harvest-600 transition-all font-medium"
            />
            <div className="absolute inset-y-0 right-0 pr-2 flex items-center space-x-2">
              <button
                type="button"
                onClick={handleVoiceSearch}
                className={`p-2.5 rounded-lg transition-colors ${
                  isListening
                    ? 'bg-red-500 text-white animate-pulse'
                    : 'text-gray-500 hover:text-harvest-700 hover:bg-gray-200/60'
                }`}
                title="Voice Search"
              >
                <Mic className="h-5 w-5" />
              </button>
              <button
                type="button"
                onClick={onApplyFilters}
                className="hidden sm:inline-flex items-center px-4 py-2.5 rounded-lg bg-harvest-700 hover:bg-harvest-800 text-white text-sm font-bold shadow-sm transition-all"
              >
                {t.searchButton}
              </button>
            </div>
          </div>

          {/* Voice Search Notification */}
          {voiceNotice && (
            <div className="mb-4 text-xs font-semibold text-harvest-800 bg-harvest-100/70 border border-harvest-200 px-3 py-1.5 rounded-lg inline-block">
              {voiceNotice}
            </div>
          )}

          {/* Quick Suggestions Chips */}
          <div className="flex flex-wrap items-center gap-2 mb-6">
            <span className="text-xs font-bold text-gray-500 flex items-center">
              <Sparkles className="w-3.5 h-3.5 mr-1 text-harvest-600" /> Suggestions:
            </span>
            {cropSuggestions.map((crop) => (
              <button
                key={crop}
                onClick={() => onSearchChange(crop)}
                className="text-xs font-semibold bg-gray-100 hover:bg-harvest-100 hover:text-harvest-800 text-gray-700 px-3 py-1 rounded-full transition-colors border border-gray-200"
              >
                {crop}
              </button>
            ))}
          </div>

          {/* Location Dropdown Grid */}
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-6 pt-2 border-t border-gray-100">
            {/* State Filter */}
            <div>
              <label className="block text-xs font-bold text-gray-700 uppercase tracking-wider mb-1.5 flex items-center">
                <MapPin className="w-3.5 h-3.5 mr-1 text-harvest-600" /> {t.state}
              </label>
              <select
                value={selectedState}
                onChange={(e) => {
                  onStateChange(e.target.value);
                  onDistrictChange('');
                  onMandiChange('');
                }}
                className="w-full bg-gray-50 border border-gray-300 rounded-lg px-3.5 py-2.5 text-sm font-semibold text-gray-800 focus:outline-none focus:ring-2 focus:ring-harvest-600 focus:bg-white"
              >
                <option value="">All States</option>
                {Object.keys(statesAndDistricts).map((st) => (
                  <option key={st} value={st}>{st}</option>
                ))}
              </select>
            </div>

            {/* District Filter */}
            <div>
              <label className="block text-xs font-bold text-gray-700 uppercase tracking-wider mb-1.5 flex items-center">
                <MapPin className="w-3.5 h-3.5 mr-1 text-harvest-600" /> {t.district}
              </label>
              <select
                value={selectedDistrict}
                onChange={(e) => {
                  onDistrictChange(e.target.value);
                  onMandiChange('');
                }}
                disabled={!selectedState}
                className="w-full bg-gray-50 border border-gray-300 rounded-lg px-3.5 py-2.5 text-sm font-semibold text-gray-800 focus:outline-none focus:ring-2 focus:ring-harvest-600 focus:bg-white disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <option value="">All Districts</option>
                {availableDistricts.map((dist) => (
                  <option key={dist} value={dist}>{dist}</option>
                ))}
              </select>
            </div>

            {/* Mandi Filter */}
            <div>
              <label className="block text-xs font-bold text-gray-700 uppercase tracking-wider mb-1.5 flex items-center">
                <MapPin className="w-3.5 h-3.5 mr-1 text-harvest-600" /> {t.mandi}
              </label>
              <select
                value={selectedMandi}
                onChange={(e) => onMandiChange(e.target.value)}
                disabled={!selectedDistrict}
                className="w-full bg-gray-50 border border-gray-300 rounded-lg px-3.5 py-2.5 text-sm font-semibold text-gray-800 focus:outline-none focus:ring-2 focus:ring-harvest-600 focus:bg-white disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <option value="">All Mandis / APMCs</option>
                {availableMandis.map((m) => (
                  <option key={m} value={m}>{m}</option>
                ))}
              </select>
            </div>
          </div>

          {/* Category Tabs & Action Buttons Row */}
          <div className="flex flex-col md:flex-row items-stretch md:items-center justify-between gap-4 pt-4 border-t border-gray-100">
            {/* Category Filter Pills */}
            <div className="flex flex-wrap items-center gap-2">
              {categories.map((cat) => {
                const isActive = selectedCategory === cat.id;
                return (
                  <button
                    key={cat.id}
                    onClick={() => onCategoryChange(cat.id)}
                    className={`inline-flex items-center space-x-1.5 px-3.5 py-2 rounded-xl text-xs sm:text-sm font-bold transition-all ${
                      isActive
                        ? 'bg-harvest-700 text-white shadow-md shadow-harvest-700/20'
                        : 'bg-gray-100 hover:bg-gray-200 text-gray-700'
                    }`}
                  >
                    <span>{cat.icon}</span>
                    <span>{cat.label}</span>
                  </button>
                );
              })}
            </div>

            {/* Apply & Reset Buttons */}
            <div className="flex items-center space-x-3 self-end md:self-auto">
              <button
                onClick={onResetFilters}
                className="inline-flex items-center space-x-1 px-4 py-2 rounded-lg border border-gray-300 hover:bg-gray-50 text-gray-700 text-xs sm:text-sm font-semibold transition-colors"
              >
                <RotateCcw className="w-4 h-4 mr-1 text-gray-500" />
                <span>{t.reset}</span>
              </button>
              <button
                onClick={onApplyFilters}
                className="inline-flex items-center space-x-1 px-5 py-2 rounded-lg bg-harvest-700 hover:bg-harvest-800 text-white text-xs sm:text-sm font-bold shadow-sm transition-all shadow-harvest-700/20"
              >
                <Filter className="w-4 h-4 mr-1" />
                <span>{t.applyFilters}</span>
              </button>
            </div>
          </div>

        </div>

      </div>
    </div>
  );
}
