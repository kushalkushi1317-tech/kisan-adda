import React, { useState } from 'react';
import { MapPin, Building2, Store, Search } from 'lucide-react';
import { statesAndDistricts } from '../data/cropData';

export default function MarketLocations({ onFilterMandi }) {
  const [activeState, setActiveState] = useState('Karnataka');
  const [searchMandi, setSearchMandi] = useState('');

  const states = Object.keys(statesAndDistricts);
  const districts = statesAndDistricts[activeState] || {};

  return (
    <section id="locations-section" className="py-12 bg-gray-50 border-b border-gray-200">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        
        {/* Header */}
        <div className="mb-8">
          <div className="flex items-center space-x-2">
            <span className="p-2 rounded-xl bg-harvest-100 text-harvest-800">
              <Building2 className="w-5 h-5 text-harvest-700" />
            </span>
            <h2 className="text-2xl sm:text-3xl font-black text-gray-900 tracking-tight">
              Regulated Market Locations (APMC Mandis)
            </h2>
          </div>
          <p className="text-sm text-gray-600 font-medium mt-1">
            Browse verified agricultural produce market committees across all supported states and districts.
          </p>
        </div>

        {/* State Selection Tabs */}
        <div className="flex items-center space-x-2 overflow-x-auto pb-4 mb-6">
          {states.map((st) => (
            <button
              key={st}
              onClick={() => setActiveState(st)}
              className={`px-4 py-2 rounded-xl text-xs sm:text-sm font-bold whitespace-nowrap transition-all ${
                activeState === st
                  ? 'bg-harvest-700 text-white shadow-md shadow-harvest-700/20'
                  : 'bg-white text-gray-700 border border-gray-200 hover:bg-gray-100'
              }`}
            >
              {st}
            </button>
          ))}
        </div>

        {/* Districts and Mandis Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {Object.entries(districts).map(([distName, mandisArray]) => (
            <div key={distName} className="bg-white rounded-2xl border border-gray-200 p-6 shadow-sm">
              <div className="flex items-center space-x-2 mb-4 pb-3 border-b border-gray-100">
                <MapPin className="w-4 h-4 text-harvest-600" />
                <h3 className="font-extrabold text-base text-gray-900">{distName} District</h3>
              </div>

              <div className="space-y-2.5">
                {mandisArray.map((mandi) => (
                  <div
                    key={mandi}
                    onClick={() => onFilterMandi(activeState, distName, mandi)}
                    className="flex items-center justify-between p-2.5 rounded-xl bg-gray-50 hover:bg-harvest-50 text-gray-800 hover:text-harvest-800 cursor-pointer transition-colors border border-gray-100 hover:border-harvest-200"
                  >
                    <div className="flex items-center space-x-2">
                      <Store className="w-4 h-4 text-gray-400" />
                      <span className="text-xs sm:text-sm font-bold">{mandi}</span>
                    </div>
                    <span className="text-xs text-harvest-700 font-bold">View Rates →</span>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>

      </div>
    </section>
  );
}
