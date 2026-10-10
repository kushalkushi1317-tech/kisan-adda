import React, { useState } from 'react';
import { Globe, User, Search, Sprout, Menu, X } from 'lucide-react';
import { translations } from '../i18n/translations';

export default function Navbar({
  currentLang,
  onLangChange,
  activeSection,
  onNavigate,
  onOpenLogin,
  onSearchFocus
}) {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const t = translations[currentLang] || translations.en;

  const navLinks = [
    { id: 'home', label: t.home },
    { id: 'prices', label: t.cropPrices },
    { id: 'locations', label: t.marketLocations },
    { id: 'trends', label: t.priceTrends },
    { id: 'about', label: t.aboutUs }
  ];

  return (
    <header className="sticky top-0 z-40 bg-white/95 backdrop-blur-md border-b border-gray-200 shadow-sm">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-20">
          {/* Logo & Tagline */}
          <div 
            className="flex items-center space-x-3 cursor-pointer group"
            onClick={() => onNavigate('home')}
          >
            <div className="w-12 h-12 rounded-xl bg-gradient-to-br from-harvest-600 to-harvest-800 flex items-center justify-center text-white shadow-md shadow-harvest-700/20 group-hover:scale-105 transition-transform">
              <Sprout className="w-7 h-7" />
            </div>
            <div>
              <div className="flex items-center space-x-2">
                <span className="text-2xl font-black text-gray-900 tracking-tight">{t.brand}</span>
                <span className="text-xs font-semibold px-2 py-0.5 rounded-full bg-harvest-100 text-harvest-800 border border-harvest-200">
                  Agri-Portal
                </span>
              </div>
              <p className="text-xs text-gray-500 font-medium hidden sm:block">
                {t.tagline}
              </p>
            </div>
          </div>

          {/* Desktop Navigation Links */}
          <nav className="hidden lg:flex items-center space-x-1">
            {navLinks.map((link) => (
              <button
                key={link.id}
                onClick={() => onNavigate(link.id)}
                className={`px-4 py-2 rounded-lg text-sm font-semibold transition-colors ${
                  activeSection === link.id
                    ? 'text-harvest-700 bg-harvest-50 shadow-sm'
                    : 'text-gray-600 hover:text-harvest-700 hover:bg-gray-50'
                }`}
              >
                {link.label}
              </button>
            ))}
          </nav>

          {/* Right Header Actions */}
          <div className="hidden sm:flex items-center space-x-3">
            {/* Quick Search trigger */}
            <button
              onClick={onSearchFocus}
              className="p-2.5 text-gray-500 hover:text-harvest-700 hover:bg-gray-100 rounded-lg transition-colors border border-gray-200"
              title="Search Crops"
            >
              <Search className="w-4 h-4" />
            </button>

            {/* Language Selector */}
            <div className="relative flex items-center bg-gray-50 border border-gray-200 rounded-lg px-2.5 py-1.5">
              <Globe className="w-4 h-4 text-gray-500 mr-2" />
              <select
                value={currentLang}
                onChange={(e) => onLangChange(e.target.value)}
                className="bg-transparent text-sm font-semibold text-gray-700 focus:outline-none cursor-pointer"
              >
                <option value="en">English (EN)</option>
                <option value="hi">हिन्दी (HI)</option>
                <option value="te">తెలుగు (TE)</option>
              </select>
            </div>

            {/* Farmer Login / Profile button */}
            <button
              onClick={onOpenLogin}
              className="inline-flex items-center space-x-2 px-4 py-2 rounded-lg bg-harvest-700 hover:bg-harvest-800 text-white text-sm font-bold shadow-sm transition-all shadow-harvest-700/20 active:scale-95"
            >
              <User className="w-4 h-4" />
              <span>{t.login}</span>
            </button>
          </div>

          {/* Mobile menu hamburger */}
          <div className="flex lg:hidden items-center space-x-2">
            <select
              value={currentLang}
              onChange={(e) => onLangChange(e.target.value)}
              className="bg-gray-100 text-xs font-bold text-gray-700 rounded-md px-2 py-1.5 border border-gray-300"
            >
              <option value="en">EN</option>
              <option value="hi">HI</option>
              <option value="te">TE</option>
            </select>

            <button
              onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
              className="p-2 text-gray-600 hover:text-gray-900 rounded-lg hover:bg-gray-100"
            >
              {mobileMenuOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
            </button>
          </div>
        </div>

        {/* Mobile Dropdown Navigation */}
        {mobileMenuOpen && (
          <div className="lg:hidden py-4 border-t border-gray-200 space-y-2 animate-in fade-in slide-in-from-top-4">
            {navLinks.map((link) => (
              <button
                key={link.id}
                onClick={() => {
                  onNavigate(link.id);
                  setMobileMenuOpen(false);
                }}
                className={`w-full text-left px-4 py-2.5 rounded-lg text-sm font-semibold ${
                  activeSection === link.id
                    ? 'text-harvest-700 bg-harvest-50 font-bold'
                    : 'text-gray-700 hover:bg-gray-50'
                }`}
              >
                {link.label}
              </button>
            ))}
            <div className="pt-2 px-4">
              <button
                onClick={() => {
                  onOpenLogin();
                  setMobileMenuOpen(false);
                }}
                className="w-full flex items-center justify-center space-x-2 py-2.5 rounded-lg bg-harvest-700 text-white font-bold text-sm shadow-sm"
              >
                <User className="w-4 h-4" />
                <span>{t.login}</span>
              </button>
            </div>
          </div>
        )}
      </div>
    </header>
  );
}
