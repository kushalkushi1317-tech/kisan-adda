import React, { useState } from 'react';
import { GitCompare, Award, MapPin, ArrowRight, CheckCircle2 } from 'lucide-react';
import { translations } from '../i18n/translations';

export default function MandiComparison({ currentLang, crops, unit, onSelectCrop }) {
  const t = translations[currentLang] || translations.en;
  const [selectedCropName, setSelectedCropName] = useState('Tomato');
  const factor = unit === 'quintal' ? 100 : 1;
  const unitLabel = unit === 'quintal' ? '₹/qtl' : '₹/kg';

  // Unique crops that have multiple mandi entries
  const cropNames = Array.from(new Set(crops.map(c => c.name)));
  const mandisForCrop = crops
    .filter(c => c.name === selectedCropName)
    .sort((a, b) => b.modalPrice - a.modalPrice);

  const highestMandi = mandisForCrop[0];
  const lowestMandi = mandisForCrop[mandisForCrop.length - 1];
  const difference = highestMandi && lowestMandi ? (highestMandi.modalPrice - lowestMandi.modalPrice) * factor : 0;

  return (
    <section id="compare-section" className="py-12 bg-white border-b border-gray-200">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        
        {/* Header */}
        <div className="mb-8">
          <div className="flex items-center space-x-2">
            <span className="p-2 rounded-xl bg-harvest-100 text-harvest-800">
              <GitCompare className="w-5 h-5 text-harvest-700" />
            </span>
            <h2 className="text-2xl sm:text-3xl font-black text-gray-900 tracking-tight">
              {t.compareMarkets}
            </h2>
          </div>
          <p className="text-sm text-gray-600 font-medium mt-1">
            Compare live spot rates for the same commodity across nearby and major terminal mandis.
          </p>
        </div>

        {/* Crop Selection Bar */}
        <div className="flex flex-wrap items-center gap-2 mb-6">
          <span className="text-xs font-bold text-gray-500 uppercase tracking-wider mr-2">Select Commodity:</span>
          {cropNames.map((name) => (
            <button
              key={name}
              onClick={() => setSelectedCropName(name)}
              className={`px-4 py-2 rounded-xl text-xs sm:text-sm font-bold transition-all ${
                name === selectedCropName
                  ? 'bg-harvest-700 text-white shadow-md shadow-harvest-700/20'
                  : 'bg-gray-100 hover:bg-gray-200 text-gray-700'
              }`}
            >
              {name}
            </button>
          ))}
        </div>

        {/* Highest Price Mandi Highlight Banner */}
        {highestMandi && mandisForCrop.length > 1 && (
          <div className="mb-8 bg-gradient-to-r from-amber-500 via-amber-600 to-harvest-700 text-white rounded-2xl p-6 shadow-lg flex flex-col md:flex-row md:items-center justify-between gap-4">
            <div className="flex items-start space-x-4">
              <div className="w-12 h-12 rounded-xl bg-white/20 backdrop-blur-md flex items-center justify-center text-white flex-shrink-0">
                <Award className="w-7 h-7" />
              </div>
              <div>
                <span className="text-xs font-black uppercase tracking-wider text-amber-200 bg-white/10 px-2.5 py-0.5 rounded-full inline-block mb-1">
                  {t.bestMarketBadge}
                </span>
                <h3 className="text-xl font-black">
                  {highestMandi.mandi} ({highestMandi.district}, {highestMandi.state})
                </h3>
                <p className="text-xs sm:text-sm text-amber-100 font-medium mt-1">
                  Offers current peak modal price of <strong className="text-white underline">₹{(highestMandi.modalPrice * factor).toLocaleString('en-IN')} {unitLabel}</strong>
                  {difference > 0 && ` (+₹${difference.toLocaleString('en-IN')} ${unitLabel} higher than lowest reporting mandi)`}.
                </p>
              </div>
            </div>

            <button
              onClick={() => onSelectCrop(highestMandi)}
              className="inline-flex items-center space-x-2 px-5 py-2.5 rounded-xl bg-white text-gray-900 font-extrabold text-sm shadow hover:bg-amber-50 transition-all self-start md:self-auto"
            >
              <span>View Mandi Intel</span>
              <ArrowRight className="w-4 h-4" />
            </button>
          </div>
        )}

        {/* Mandis Comparison Table */}
        <div className="bg-white rounded-2xl border border-gray-200 shadow-sm overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-left border-collapse">
              <thead>
                <tr className="bg-gray-50 border-b border-gray-200 text-xs font-bold text-gray-600 uppercase tracking-wider">
                  <th className="py-4 px-6">Mandi Name</th>
                  <th className="py-4 px-6">State & District</th>
                  <th className="py-4 px-4 text-right">Min Rate</th>
                  <th className="py-4 px-4 text-right">Max Rate</th>
                  <th className="py-4 px-6 text-right">Modal Rate ({unitLabel})</th>
                  <th className="py-4 px-4 text-center">Status</th>
                  <th className="py-4 px-6 text-center">Action</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-100 text-sm">
                {mandisForCrop.map((mandi, idx) => {
                  const isTop = idx === 0;
                  const modalRate = (mandi.modalPrice * factor).toLocaleString('en-IN');
                  const minRate = (mandi.minPrice * factor).toLocaleString('en-IN');
                  const maxRate = (mandi.maxPrice * factor).toLocaleString('en-IN');

                  return (
                    <tr 
                      key={mandi.id}
                      className={isTop ? 'bg-amber-50/40 font-semibold' : 'hover:bg-gray-50'}
                    >
                      <td className="py-4 px-6 whitespace-nowrap">
                        <div className="flex items-center space-x-2">
                          {isTop && <Award className="w-4 h-4 text-amber-600 flex-shrink-0" />}
                          <span className="font-bold text-gray-900">{mandi.mandi}</span>
                        </div>
                      </td>
                      <td className="py-4 px-6 whitespace-nowrap text-gray-600">
                        {mandi.district}, {mandi.state}
                      </td>
                      <td className="py-4 px-4 text-right text-gray-600 whitespace-nowrap">
                        ₹{minRate}
                      </td>
                      <td className="py-4 px-4 text-right text-gray-600 whitespace-nowrap">
                        ₹{maxRate}
                      </td>
                      <td className="py-4 px-6 text-right whitespace-nowrap">
                        <span className={`text-base font-black px-2.5 py-1 rounded-lg ${
                          isTop ? 'bg-amber-100 text-amber-900 font-black' : 'text-harvest-800'
                        }`}>
                          ₹{modalRate}
                        </span>
                      </td>
                      <td className="py-4 px-4 text-center whitespace-nowrap">
                        {isTop ? (
                          <span className="inline-flex items-center text-xs font-bold text-amber-800 bg-amber-100 px-2 py-0.5 rounded-full">
                            Top Rate ⭐
                          </span>
                        ) : (
                          <span className="text-xs text-gray-500 font-medium">Standard</span>
                        )}
                      </td>
                      <td className="py-4 px-6 text-center whitespace-nowrap">
                        <button
                          onClick={() => onSelectCrop(mandi)}
                          className="text-xs font-bold text-harvest-700 hover:text-harvest-900 underline"
                        >
                          View Trends
                        </button>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>

      </div>
    </section>
  );
}
