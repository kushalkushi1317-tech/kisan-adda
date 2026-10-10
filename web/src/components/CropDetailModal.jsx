import React from 'react';
import { X, TrendingUp, TrendingDown, MapPin, Clock, PackageCheck, Truck } from 'lucide-react';
import { translations } from '../i18n/translations';

export default function CropDetailModal({ crop, isOpen, onClose, currentLang, unit }) {
  if (!isOpen || !crop) return null;

  const t = translations[currentLang] || translations.en;
  const factor = unit === 'quintal' ? 100 : 1;
  const unitLabel = unit === 'quintal' ? '₹/qtl' : '₹/kg';
  const isUp = crop.priceChange >= 0;

  const localizedName = currentLang === 'hi' && crop.nameHi
    ? crop.nameHi
    : currentLang === 'te' && crop.nameTe
    ? crop.nameTe
    : crop.name;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-gray-900/60 backdrop-blur-sm animate-in fade-in">
      <div className="bg-white rounded-3xl max-w-xl w-full p-6 sm:p-8 shadow-2xl border border-gray-100 relative max-h-[90vh] overflow-y-auto">
        <button
          onClick={onClose}
          className="absolute top-5 right-5 p-2 text-gray-400 hover:text-gray-600 rounded-full hover:bg-gray-100"
        >
          <X className="w-5 h-5" />
        </button>

        {/* Header */}
        <div className="flex items-start space-x-4 mb-6">
          <div className="w-16 h-16 rounded-2xl bg-harvest-50 border border-harvest-200 flex items-center justify-center text-4xl flex-shrink-0">
            {crop.icon}
          </div>
          <div>
            <div className="flex items-center space-x-2">
              <h3 className="text-2xl font-black text-gray-900">{localizedName}</h3>
              <span className={`inline-flex items-center text-xs font-bold px-2.5 py-0.5 rounded-full ${
                isUp ? 'bg-emerald-100 text-emerald-800' : 'bg-red-100 text-red-800'
              }`}>
                {isUp ? <TrendingUp className="w-3.5 h-3.5 mr-1" /> : <TrendingDown className="w-3.5 h-3.5 mr-1" />}
                {isUp ? `+${crop.priceChange}%` : `${crop.priceChange}%`}
              </span>
            </div>
            <p className="text-sm text-gray-500 font-medium">
              Category: {crop.category} • Standard Grade: {crop.grade || 'A'}
            </p>
          </div>
        </div>

        {/* Location & Mandi Info */}
        <div className="p-4 bg-gray-50 rounded-2xl border border-gray-100 mb-6 flex items-start space-x-3">
          <MapPin className="w-5 h-5 text-harvest-600 mt-0.5 flex-shrink-0" />
          <div className="text-sm">
            <span className="font-extrabold text-gray-900 block">{crop.mandi}</span>
            <span className="text-gray-500">{crop.district} District, {crop.state} State</span>
            <div className="mt-2 flex items-center space-x-4 text-xs text-gray-600">
              <span className="flex items-center">
                <Truck className="w-3.5 h-3.5 mr-1 text-gray-400" /> Daily Arrivals: {crop.arrivals || '120 MT'}
              </span>
              <span className="flex items-center">
                <Clock className="w-3.5 h-3.5 mr-1 text-gray-400" /> {crop.lastUpdated}
              </span>
            </div>
          </div>
        </div>

        {/* Pricing Cards */}
        <div className="grid grid-cols-3 gap-3 mb-6 text-center">
          <div className="p-3 bg-gray-50 rounded-xl border border-gray-200">
            <span className="text-[11px] font-bold text-gray-400 uppercase">Min Rate</span>
            <div className="text-base font-extrabold text-gray-800 mt-1">₹{(crop.minPrice * factor).toLocaleString('en-IN')}</div>
            <span className="text-[10px] text-gray-400 font-semibold">{unitLabel}</span>
          </div>

          <div className="p-3 bg-harvest-50 rounded-xl border border-harvest-200">
            <span className="text-[11px] font-bold text-harvest-700 uppercase">Modal Rate</span>
            <div className="text-xl font-black text-harvest-800 mt-0.5">₹{(crop.modalPrice * factor).toLocaleString('en-IN')}</div>
            <span className="text-[10px] text-harvest-600 font-bold">{unitLabel}</span>
          </div>

          <div className="p-3 bg-gray-50 rounded-xl border border-gray-200">
            <span className="text-[11px] font-bold text-gray-400 uppercase">Max Rate</span>
            <div className="text-base font-extrabold text-gray-800 mt-1">₹{(crop.maxPrice * factor).toLocaleString('en-IN')}</div>
            <span className="text-[10px] text-gray-400 font-semibold">{unitLabel}</span>
          </div>
        </div>

        {/* Quintal vs Kg Conversion Box */}
        <div className="p-4 bg-amber-50/70 border border-amber-200/80 rounded-2xl mb-6">
          <h4 className="text-xs font-black text-amber-900 uppercase tracking-wider mb-2">Unit Equivalents:</h4>
          <div className="flex items-center justify-between text-xs sm:text-sm font-bold text-gray-800">
            <span>Rate Per Kilogram: <strong className="text-harvest-800">₹{crop.modalPrice}/kg</strong></span>
            <span className="text-gray-300">|</span>
            <span>Rate Per Quintal (100 kg): <strong className="text-harvest-800">₹{(crop.modalPrice * 100).toLocaleString('en-IN')}/qtl</strong></span>
          </div>
        </div>

        {/* 7-Day History Mini Logs */}
        {crop.trends?.["7d"] && (
          <div>
            <h4 className="text-xs font-bold text-gray-500 uppercase tracking-wider mb-3">7-Day Spot Price Log</h4>
            <div className="space-y-1.5">
              {crop.trends["7d"].map((log, i) => (
                <div key={i} className="flex items-center justify-between p-2 rounded-lg bg-gray-50 text-xs font-medium text-gray-700">
                  <span>{log.day}</span>
                  <span className="font-bold text-gray-900">₹{(log.price * factor).toLocaleString('en-IN')} {unitLabel}</span>
                </div>
              ))}
            </div>
          </div>
        )}

        <div className="mt-8 pt-4 border-t border-gray-100">
          <button
            onClick={onClose}
            className="w-full py-3 rounded-xl bg-gray-900 hover:bg-black text-white text-sm font-bold transition-colors"
          >
            Close Details
          </button>
        </div>

      </div>
    </div>
  );
}
