import React from 'react';
import { TrendingUp, TrendingDown, Eye, Clock, MapPin } from 'lucide-react';
import { translations } from '../i18n/translations';

export default function PriceTable({ currentLang, crops, unit, onSelectCrop }) {
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
    <div className="bg-white rounded-2xl border border-gray-200 shadow-sm overflow-hidden">
      <div className="overflow-x-auto">
        <table className="w-full text-left border-collapse">
          <thead>
            <tr className="bg-gray-50/80 border-b border-gray-200 text-xs font-bold text-gray-600 uppercase tracking-wider">
              <th scope="col" className="py-4 px-6">Commodity / Crop</th>
              <th scope="col" className="py-4 px-6">Mandi / Location</th>
              <th scope="col" className="py-4 px-4 text-right">Min Price</th>
              <th scope="col" className="py-4 px-4 text-right">Max Price</th>
              <th scope="col" className="py-4 px-6 text-right">Modal / Avg ({unitLabel})</th>
              <th scope="col" className="py-4 px-4 text-center">24h Change</th>
              <th scope="col" className="py-4 px-4">Updated</th>
              <th scope="col" className="py-4 px-6 text-center">Action</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100 text-sm">
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
                <tr 
                  key={crop.id} 
                  className="hover:bg-harvest-50/40 transition-colors group"
                >
                  {/* Crop column */}
                  <td className="py-4 px-6 whitespace-nowrap">
                    <div className="flex items-center space-x-3">
                      <div className="w-10 h-10 rounded-xl bg-gray-100 flex items-center justify-center text-xl flex-shrink-0 group-hover:bg-white group-hover:shadow-sm border border-transparent group-hover:border-harvest-200 transition-all">
                        {crop.icon}
                      </div>
                      <div>
                        <div className="font-bold text-gray-900 group-hover:text-harvest-700 transition-colors">
                          {localizedName}
                        </div>
                        <span className="text-xs text-gray-500 font-medium">
                          {crop.category} • {crop.grade || 'Standard'}
                        </span>
                      </div>
                    </div>
                  </td>

                  {/* Location column */}
                  <td className="py-4 px-6 whitespace-nowrap">
                    <div className="flex items-start space-x-1.5">
                      <MapPin className="w-3.5 h-3.5 text-harvest-600 mt-0.5 flex-shrink-0" />
                      <div>
                        <div className="font-semibold text-gray-800">{crop.mandi}</div>
                        <div className="text-xs text-gray-500">{crop.district}, {crop.state}</div>
                      </div>
                    </div>
                  </td>

                  {/* Min price */}
                  <td className="py-4 px-4 text-right font-medium text-gray-600 whitespace-nowrap">
                    ₹{minVal}
                  </td>

                  {/* Max price */}
                  <td className="py-4 px-4 text-right font-medium text-gray-600 whitespace-nowrap">
                    ₹{maxVal}
                  </td>

                  {/* Modal price */}
                  <td className="py-4 px-6 text-right whitespace-nowrap">
                    <span className="font-extrabold text-base text-harvest-800 bg-harvest-50 px-2.5 py-1 rounded-lg border border-harvest-200">
                      ₹{modalVal}
                    </span>
                  </td>

                  {/* Price movement */}
                  <td className="py-4 px-4 whitespace-nowrap text-center">
                    <span className={`inline-flex items-center text-xs font-bold px-2 py-1 rounded-full ${
                      isUp ? 'bg-emerald-100 text-emerald-800' : 'bg-red-100 text-red-800'
                    }`}>
                      {isUp ? <TrendingUp className="w-3.5 h-3.5 mr-1" /> : <TrendingDown className="w-3.5 h-3.5 mr-1" />}
                      {isUp ? `+${crop.priceChange}%` : `${crop.priceChange}%`}
                    </span>
                  </td>

                  {/* Last updated */}
                  <td className="py-4 px-4 whitespace-nowrap text-xs text-gray-500 font-medium">
                    <div className="flex items-center space-x-1">
                      <Clock className="w-3 h-3 text-gray-400" />
                      <span>{crop.lastUpdated}</span>
                    </div>
                  </td>

                  {/* View details button */}
                  <td className="py-4 px-6 text-center whitespace-nowrap">
                    <button
                      onClick={() => onSelectCrop(crop)}
                      className="inline-flex items-center space-x-1 px-3 py-1.5 rounded-lg bg-gray-100 hover:bg-harvest-700 hover:text-white text-gray-700 text-xs font-bold transition-all shadow-sm"
                    >
                      <Eye className="w-3.5 h-3.5" />
                      <span>{t.viewDetails}</span>
                    </button>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
}
