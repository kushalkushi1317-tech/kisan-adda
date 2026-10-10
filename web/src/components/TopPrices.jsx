import React from 'react';
import { TrendingUp, TrendingDown, ArrowUpRight, Award } from 'lucide-react';
import { translations } from '../i18n/translations';

export default function TopPrices({ currentLang, crops, unit, onSelectCrop }) {
  const t = translations[currentLang] || translations.en;
  const factor = unit === 'quintal' ? 100 : 1;
  const unitLabel = unit === 'quintal' ? '₹/qtl' : '₹/kg';

  // Sort by highest modal price
  const topCrops = [...crops].sort((a, b) => b.modalPrice - a.modalPrice).slice(0, 4);

  return (
    <section className="py-10 bg-white border-b border-gray-200">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-6">
          <div>
            <div className="flex items-center space-x-2">
              <span className="p-1.5 rounded-lg bg-amber-100 text-amber-800">
                <Award className="w-5 h-5 text-amber-600" />
              </span>
              <h2 className="text-2xl font-black text-gray-900 tracking-tight">{t.topPricesTitle}</h2>
            </div>
            <p className="text-sm text-gray-500 font-medium mt-1">
              {t.topPricesSubtitle}
            </p>
          </div>
          <span className="text-xs font-bold text-gray-500 bg-gray-100 px-3 py-1.5 rounded-lg border border-gray-200 self-start sm:self-auto">
            Unit: {unitLabel}
          </span>
        </div>

        {/* 4 Cards Grid */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
          {topCrops.map((crop) => {
            const isUp = crop.priceChange >= 0;
            const modalDisplay = (crop.modalPrice * factor).toLocaleString('en-IN');
            const minDisplay = (crop.minPrice * factor).toLocaleString('en-IN');
            const maxDisplay = (crop.maxPrice * factor).toLocaleString('en-IN');
            
            const localizedName = currentLang === 'hi' && crop.nameHi
              ? crop.nameHi
              : currentLang === 'te' && crop.nameTe
              ? crop.nameTe
              : crop.name;

            return (
              <div
                key={crop.id}
                onClick={() => onSelectCrop(crop)}
                className="group relative bg-gradient-to-br from-white to-gray-50/50 rounded-2xl p-5 border border-gray-200 hover:border-harvest-500 hover:shadow-xl transition-all cursor-pointer"
              >
                {/* Header: Icon, Crop Name & Grade */}
                <div className="flex items-start justify-between mb-4">
                  <div className="flex items-center space-x-3">
                    <div className="w-12 h-12 rounded-xl bg-harvest-50 border border-harvest-200 flex items-center justify-center text-2xl group-hover:scale-110 transition-transform">
                      {crop.icon}
                    </div>
                    <div>
                      <h3 className="font-extrabold text-base text-gray-900 group-hover:text-harvest-700 transition-colors">
                        {localizedName}
                      </h3>
                      <p className="text-xs text-gray-500 font-medium">{crop.grade || crop.category}</p>
                    </div>
                  </div>
                  <span className={`inline-flex items-center text-xs font-bold px-2 py-0.5 rounded-full ${
                    isUp ? 'bg-emerald-100 text-emerald-800' : 'bg-red-100 text-red-800'
                  }`}>
                    {isUp ? <TrendingUp className="w-3 h-3 mr-0.5" /> : <TrendingDown className="w-3 h-3 mr-0.5" />}
                    {isUp ? `+${crop.priceChange}%` : `${crop.priceChange}%`}
                  </span>
                </div>

                {/* Mandi & Location */}
                <div className="mb-4 text-xs text-gray-600 bg-gray-100/70 rounded-lg p-2.5">
                  <span className="font-bold text-gray-900 block truncate">{crop.mandi}</span>
                  <span className="text-gray-500 text-[11px]">{crop.district}, {crop.state}</span>
                </div>

                {/* Main Modal Price */}
                <div className="flex items-baseline justify-between pt-2 border-t border-gray-200">
                  <div>
                    <span className="text-[11px] font-bold text-gray-400 uppercase tracking-wider block">Modal Rate</span>
                    <span className="text-2xl font-black text-harvest-800">₹{modalDisplay}</span>
                    <span className="text-xs text-gray-500 ml-1 font-semibold">{unitLabel}</span>
                  </div>
                  <div className="text-right">
                    <span className="text-[10px] text-gray-400 block">Min - Max</span>
                    <span className="text-xs font-bold text-gray-700">₹{minDisplay} - ₹{maxDisplay}</span>
                  </div>
                </div>

                {/* Click affordance */}
                <div className="mt-3 flex items-center justify-end text-xs font-bold text-harvest-700 group-hover:translate-x-1 transition-transform">
                  <span>Analyze</span>
                  <ArrowUpRight className="w-3.5 h-3.5 ml-0.5" />
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </section>
  );
}
