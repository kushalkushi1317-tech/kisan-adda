import React from 'react';
import { TrendingUp, TrendingDown, Eye, Clock, MapPin, ArrowRight } from 'lucide-react';
import { translations } from '../i18n/translations';

export default function PriceCards({ currentLang, crops, unit, onSelectCrop }) {
  const t = translations[currentLang] || translations.en;
  const factor = unit === 'quintal' ? 100 : 1;
  const unitLabel = unit === 'quintal' ? '₹/qtl' : '₹/kg';

  if (crops.length === 0) {
    return (
      <div className="bg-white rounded-2xl border border-gray-200 p-12 text-center">
        <span className="text-4xl mb-3 block">🌾</span>
        <h3 className="text-lg font-bold text-gray-900 mb-1">No Crop Prices Found</h3>
        <p className="text-sm text-gray-500">Try adjusting your search query, state, district or category filters.</p>
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      {crops.map((crop) => {
        const isUp = crop.priceChange >= 0;
        const modalVal = (crop.modalPrice * factor).toLocaleString('en-IN');
        const minVal = (crop.minPrice * factor).toLocaleString('en-IN');
        const maxVal = (crop.maxPrice * factor).toLocaleString('en-IN');

        const localizedName = currentLang === 'hi' && crop.nameHi
          ? crop.nameHi
          : currentLang === 'te' && crop.nameTe
          ? crop.nameTe
          : crop.name;

        return (
          <div
            key={crop.id}
            className="bg-white rounded-2xl border border-gray-200 hover:border-harvest-400 p-6 shadow-sm hover:shadow-xl transition-all flex flex-col justify-between group"
          >
            <div>
              {/* Top Header */}
              <div className="flex items-start justify-between mb-4">
                <div className="flex items-center space-x-3.5">
                  <div className="w-14 h-14 rounded-2xl bg-harvest-50 border border-harvest-200 flex items-center justify-center text-3xl group-hover:scale-105 transition-transform">
                    {crop.icon}
                  </div>
                  <div>
                    <h3 className="font-extrabold text-lg text-gray-900 group-hover:text-harvest-700 transition-colors">
                      {localizedName}
                    </h3>
                    <span className="inline-block text-xs font-semibold text-gray-500 bg-gray-100 px-2 py-0.5 rounded-md mt-0.5">
                      {crop.category} • {crop.grade || 'Grade A'}
                    </span>
                  </div>
                </div>

                <span className={`inline-flex items-center text-xs font-bold px-2.5 py-1 rounded-full ${
                  isUp ? 'bg-emerald-100 text-emerald-800' : 'bg-red-100 text-red-800'
                }`}>
                  {isUp ? <TrendingUp className="w-3.5 h-3.5 mr-1" /> : <TrendingDown className="w-3.5 h-3.5 mr-1" />}
                  {isUp ? `+${crop.priceChange}%` : `${crop.priceChange}%`}
                </span>
              </div>

              {/* Mandi & Location */}
              <div className="mb-4 bg-gray-50 rounded-xl p-3 border border-gray-100 flex items-start space-x-2">
                <MapPin className="w-4 h-4 text-harvest-600 mt-0.5 flex-shrink-0" />
                <div className="text-xs">
                  <div className="font-bold text-gray-900">{crop.mandi}</div>
                  <div className="text-gray-500">{crop.district}, {crop.state}</div>
                </div>
              </div>

              {/* Pricing Grid */}
              <div className="grid grid-cols-3 gap-2 bg-gradient-to-r from-gray-50 to-harvest-50/50 rounded-xl p-3 border border-gray-200/60 mb-4 text-center">
                <div>
                  <span className="text-[10px] font-bold text-gray-400 uppercase tracking-wider block">Min</span>
                  <span className="text-sm font-bold text-gray-700">₹{minVal}</span>
                </div>
                <div className="border-x border-gray-200 px-1">
                  <span className="text-[10px] font-bold text-harvest-700 uppercase tracking-wider block">Modal Avg</span>
                  <span className="text-base font-black text-harvest-800">₹{modalVal}</span>
                </div>
                <div>
                  <span className="text-[10px] font-bold text-gray-400 uppercase tracking-wider block">Max</span>
                  <span className="text-sm font-bold text-gray-700">₹{maxVal}</span>
                </div>
              </div>
            </div>

            {/* Bottom Actions */}
            <div className="pt-3 border-t border-gray-100 flex items-center justify-between text-xs text-gray-500 font-medium">
              <div className="flex items-center space-x-1">
                <Clock className="w-3.5 h-3.5 text-gray-400" />
                <span>{crop.lastUpdated}</span>
              </div>

              <button
                onClick={() => onSelectCrop(crop)}
                className="inline-flex items-center space-x-1.5 px-3 py-1.5 rounded-lg bg-harvest-50 hover:bg-harvest-700 hover:text-white text-harvest-700 font-bold transition-all"
              >
                <span>{t.viewDetails}</span>
                <ArrowRight className="w-3.5 h-3.5" />
              </button>
            </div>
          </div>
        );
      })}
    </div>
  );
}
