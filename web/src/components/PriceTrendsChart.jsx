import React, { useState } from 'react';
import { LineChart, Line, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid, Legend } from 'recharts';
import { TrendingUp, Calendar, Info, ArrowUpRight, BarChart3 } from 'lucide-react';
import { translations } from '../i18n/translations';

export default function PriceTrendsChart({ currentLang, crops, unit }) {
  const t = translations[currentLang] || translations.en;
  const [selectedCropName, setSelectedCropName] = useState('Tomato');
  const [selectedRange, setSelectedRange] = useState('7d');
  const factor = unit === 'quintal' ? 100 : 1;
  const unitLabel = unit === 'quintal' ? '₹/qtl' : '₹/kg';

  // Get unique crop names
  const cropNames = Array.from(new Set(crops.map(c => c.name)));

  // Selected crop instance
  const cropInstance = crops.find(c => c.name === selectedCropName) || crops[0];
  
  // Also find other mandis for the same crop to enable multi-mandi comparison in the chart!
  const siblingMandis = crops.filter(c => c.name === selectedCropName);

  // Prepare trend data points for chart
  const baseTrends = cropInstance?.trends?.[selectedRange] || [];
  
  const chartData = baseTrends.map((pt, idx) => {
    const item = {
      name: pt.day,
      [cropInstance.mandi]: Math.round(pt.price * factor)
    };

    // If there is another mandi with this crop, show comparison curve!
    if (siblingMandis.length > 1) {
      siblingMandis.forEach((sib, sibIdx) => {
        if (sib.id !== cropInstance.id) {
          const sibTrend = sib.trends?.[selectedRange]?.[idx];
          if (sibTrend) {
            item[sib.mandi] = Math.round(sibTrend.price * factor);
          }
        }
      });
    }
    return item;
  });

  // Calculate percentage change
  const startPrice = baseTrends[0]?.price * factor || 0;
  const endPrice = baseTrends[baseTrends.length - 1]?.price * factor || 0;
  const percentChange = startPrice > 0 ? (((endPrice - startPrice) / startPrice) * 100).toFixed(1) : 0;
  const isUp = Number(percentChange) >= 0;

  const explanation = isUp
    ? `${selectedCropName} prices at ${cropInstance.mandi} have increased by +${percentChange}% over the selected ${selectedRange === '7d' ? '7 days' : selectedRange === '30d' ? '30 days' : '90 days'}. Higher arrivals from rural districts are expected to stabilize spot pricing next week.`
    : `${selectedCropName} prices at ${cropInstance.mandi} have decreased by ${percentChange}% over the selected period due to heavy bumper arrivals and monsoon harvest cycles. Consider cold storage if available.`;

  return (
    <section id="trends-section" className="py-12 bg-gray-50 border-b border-gray-200">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        
        {/* Section Header */}
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-8">
          <div>
            <div className="flex items-center space-x-2">
              <span className="p-2 rounded-xl bg-harvest-100 text-harvest-800">
                <BarChart3 className="w-5 h-5 text-harvest-700" />
              </span>
              <h2 className="text-2xl sm:text-3xl font-black text-gray-900 tracking-tight">
                {t.trendHeading}
              </h2>
            </div>
            <p className="text-sm text-gray-600 font-medium mt-1">
              {t.trendSubtitle}
            </p>
          </div>

          {/* Timeframe Buttons */}
          <div className="flex items-center bg-white border border-gray-200 rounded-xl p-1 shadow-sm self-start md:self-auto">
            {['7d', '30d', '90d'].map((range) => (
              <button
                key={range}
                onClick={() => setSelectedRange(range)}
                className={`px-4 py-2 rounded-lg text-xs font-bold transition-all ${
                  selectedRange === range
                    ? 'bg-harvest-700 text-white shadow-sm'
                    : 'text-gray-600 hover:text-gray-900 hover:bg-gray-50'
                }`}
              >
                {range === '7d' ? '7 Days' : range === '30d' ? '30 Days' : '90 Days'}
              </button>
            ))}
          </div>
        </div>

        {/* Main Trends Container */}
        <div className="bg-white rounded-2xl border border-gray-200 shadow-sm p-6 lg:p-8">
          
          {/* Crop Selector Chips */}
          <div className="flex flex-wrap items-center gap-2 mb-6">
            <span className="text-xs font-bold text-gray-500 uppercase tracking-wider mr-2">Select Crop:</span>
            {cropNames.map((name) => {
              const isActive = name === selectedCropName;
              return (
                <button
                  key={name}
                  onClick={() => setSelectedCropName(name)}
                  className={`px-3.5 py-1.5 rounded-lg text-xs font-bold transition-all ${
                    isActive
                      ? 'bg-harvest-700 text-white shadow-sm'
                      : 'bg-gray-100 hover:bg-gray-200 text-gray-700'
                  }`}
                >
                  {name}
                </button>
              );
            })}
          </div>

          {/* Explanatory Banner */}
          <div className="mb-6 p-4 rounded-xl bg-gradient-to-r from-harvest-50 to-amber-50/50 border border-harvest-200/60 flex items-start space-x-3">
            <Info className="w-5 h-5 text-harvest-700 mt-0.5 flex-shrink-0" />
            <div className="text-sm">
              <span className="font-bold text-gray-900 block mb-0.5">Price Trend Analysis & Insight</span>
              <p className="text-gray-700 font-medium leading-relaxed">{explanation}</p>
            </div>
          </div>

          {/* Recharts Chart Area */}
          <div className="h-80 sm:h-96 w-full pt-4">
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={chartData} margin={{ top: 10, right: 30, left: 0, bottom: 10 }}>
                <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                <XAxis 
                  dataKey="name" 
                  tick={{ fontSize: 12, fill: '#6b7280', fontWeight: 600 }}
                  axisLine={{ stroke: '#e5e7eb' }}
                />
                <YAxis 
                  unit={` ${unitLabel}`}
                  tick={{ fontSize: 12, fill: '#6b7280' }}
                  axisLine={{ stroke: '#e5e7eb' }}
                  domain={['auto', 'auto']}
                />
                <Tooltip 
                  contentStyle={{ 
                    backgroundColor: '#ffffff', 
                    borderRadius: '12px', 
                    border: '1px solid #e5e7eb',
                    boxShadow: '0 10px 15px -3px rgba(0, 0, 0, 0.1)'
                  }} 
                />
                <Legend verticalAlign="top" height={36} />
                <Line 
                  type="monotone" 
                  dataKey={cropInstance.mandi} 
                  stroke="#1b5e20" 
                  strokeWidth={3} 
                  dot={{ r: 5, fill: '#1b5e20' }} 
                  activeDot={{ r: 8 }} 
                />
                {siblingMandis.filter(s => s.id !== cropInstance.id).map((sib, i) => (
                  <Line 
                    key={sib.id}
                    type="monotone" 
                    dataKey={sib.mandi} 
                    stroke={i === 0 ? '#d97706' : '#2563eb'} 
                    strokeWidth={2} 
                    strokeDasharray="4 4"
                    dot={{ r: 4 }} 
                  />
                ))}
              </LineChart>
            </ResponsiveContainer>
          </div>

          {/* Quick Metrics Bar */}
          <div className="grid grid-cols-2 sm:grid-cols-4 gap-4 mt-8 pt-6 border-t border-gray-100 text-center">
            <div className="p-3 bg-gray-50 rounded-xl">
              <span className="text-[11px] font-bold text-gray-500 uppercase">Period Start</span>
              <div className="text-lg font-bold text-gray-800">₹{Math.round(startPrice)} {unitLabel}</div>
            </div>
            <div className="p-3 bg-gray-50 rounded-xl">
              <span className="text-[11px] font-bold text-gray-500 uppercase">Current Modal</span>
              <div className="text-lg font-bold text-harvest-800">₹{Math.round(endPrice)} {unitLabel}</div>
            </div>
            <div className="p-3 bg-gray-50 rounded-xl">
              <span className="text-[11px] font-bold text-gray-500 uppercase">Trend Trajectory</span>
              <div className={`text-lg font-bold ${isUp ? 'text-emerald-700' : 'text-red-700'}`}>
                {isUp ? `+${percentChange}% (Rising)` : `${percentChange}% (Cooling)`}
              </div>
            </div>
            <div className="p-3 bg-gray-50 rounded-xl">
              <span className="text-[11px] font-bold text-gray-500 uppercase">Mandis Compared</span>
              <div className="text-lg font-bold text-gray-800">{siblingMandis.length} Active Mandis</div>
            </div>
          </div>

        </div>

      </div>
    </section>
  );
}
