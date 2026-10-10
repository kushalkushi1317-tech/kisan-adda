import React, { useState } from 'react';
import { Calculator, Truck, IndianRupee, AlertCircle, Info } from 'lucide-react';

export default function TransportCalculator({ currentCrop, unit }) {
  const [quantity, setQuantity] = useState(50); // e.g. 50 quintals or kg
  const [unitType, setUnitType] = useState('quintal'); // 'quintal' or 'kg'
  const [pricePerUnit, setPricePerUnit] = useState(
    unitType === 'quintal' ? (currentCrop?.modalPrice || 35) * 100 : (currentCrop?.modalPrice || 35)
  );
  const [transportCost, setTransportCost] = useState(3500); // in Rs
  const [otherExpenses, setOtherExpenses] = useState(1200); // loading, mandi fee, etc.

  // Calculations
  const totalRevenue = quantity * pricePerUnit;
  const totalDeductions = Number(transportCost) + Number(otherExpenses);
  const netProceeds = totalRevenue - totalDeductions;
  const netProceedsPerUnit = quantity > 0 ? (netProceeds / quantity).toFixed(1) : 0;
  const deductionRatio = totalRevenue > 0 ? ((totalDeductions / totalRevenue) * 100).toFixed(1) : 0;

  return (
    <div className="bg-white rounded-2xl border border-gray-200 p-6 shadow-sm">
      <div className="flex items-center space-x-2 mb-2">
        <div className="w-9 h-9 rounded-xl bg-harvest-100 text-harvest-800 flex items-center justify-center">
          <Calculator className="w-5 h-5 text-harvest-700" />
        </div>
        <div>
          <h3 className="font-extrabold text-lg text-gray-900">
            Transportation & Net Realization Calculator
          </h3>
          <p className="text-xs text-gray-500">
            Estimate your net take-home earnings after freight and APMC cess before traveling to a distant mandi.
          </p>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mt-6">
        {/* Quantity */}
        <div>
          <label className="block text-xs font-bold text-gray-700 uppercase tracking-wider mb-1.5">
            Quantity to Sell
          </label>
          <div className="flex rounded-xl overflow-hidden border border-gray-300">
            <input
              type="number"
              min="1"
              value={quantity}
              onChange={(e) => setQuantity(Math.max(1, parseFloat(e.target.value) || 0))}
              className="w-full px-3 py-2.5 text-sm font-bold text-gray-900 bg-gray-50 focus:bg-white focus:outline-none"
            />
            <select
              value={unitType}
              onChange={(e) => {
                const newUnit = e.target.value;
                setUnitType(newUnit);
                if (newUnit === 'quintal' && unitType === 'kg') {
                  setPricePerUnit(pricePerUnit * 100);
                } else if (newUnit === 'kg' && unitType === 'quintal') {
                  setPricePerUnit(pricePerUnit / 100);
                }
              }}
              className="bg-gray-100 text-xs font-bold text-gray-700 px-2.5 border-l border-gray-300 focus:outline-none"
            >
              <option value="quintal">Quintal</option>
              <option value="kg">Kilogram</option>
            </select>
          </div>
        </div>

        {/* Expected Price per Unit */}
        <div>
          <label className="block text-xs font-bold text-gray-700 uppercase tracking-wider mb-1.5">
            Mandi Price (₹/{unitType})
          </label>
          <div className="relative">
            <span className="absolute inset-y-0 left-0 pl-3 flex items-center text-xs text-gray-400 font-bold">
              ₹
            </span>
            <input
              type="number"
              min="0"
              value={pricePerUnit}
              onChange={(e) => setPricePerUnit(parseFloat(e.target.value) || 0)}
              className="w-full pl-7 pr-3 py-2.5 border border-gray-300 rounded-xl text-sm font-bold text-gray-900 bg-gray-50 focus:bg-white focus:outline-none focus:ring-2 focus:ring-harvest-600"
            />
          </div>
        </div>

        {/* Transportation Cost */}
        <div>
          <label className="block text-xs font-bold text-gray-700 uppercase tracking-wider mb-1.5">
            Estimated Freight / Diesel (₹)
          </label>
          <div className="relative">
            <span className="absolute inset-y-0 left-0 pl-3 flex items-center text-xs text-gray-400 font-bold">
              ₹
            </span>
            <input
              type="number"
              min="0"
              value={transportCost}
              onChange={(e) => setTransportCost(parseFloat(e.target.value) || 0)}
              className="w-full pl-7 pr-3 py-2.5 border border-gray-300 rounded-xl text-sm font-bold text-gray-900 bg-gray-50 focus:bg-white focus:outline-none focus:ring-2 focus:ring-harvest-600"
            />
          </div>
        </div>

        {/* Other Expenses (Labour, Toll, Mandi Cess) */}
        <div>
          <label className="block text-xs font-bold text-gray-700 uppercase tracking-wider mb-1.5">
            Other Expenses (Cess / Hamali) (₹)
          </label>
          <div className="relative">
            <span className="absolute inset-y-0 left-0 pl-3 flex items-center text-xs text-gray-400 font-bold">
              ₹
            </span>
            <input
              type="number"
              min="0"
              value={otherExpenses}
              onChange={(e) => setOtherExpenses(parseFloat(e.target.value) || 0)}
              className="w-full pl-7 pr-3 py-2.5 border border-gray-300 rounded-xl text-sm font-bold text-gray-900 bg-gray-50 focus:bg-white focus:outline-none focus:ring-2 focus:ring-harvest-600"
            />
          </div>
        </div>
      </div>

      {/* Results Bar */}
      <div className="mt-6 p-5 rounded-2xl bg-gradient-to-r from-harvest-50 via-harvest-100/50 to-amber-50 border border-harvest-200">
        <div className="grid grid-cols-1 sm:grid-cols-4 gap-4 text-center sm:text-left">
          <div>
            <span className="text-[11px] font-bold text-gray-500 uppercase">Gross Auction Revenue</span>
            <div className="text-xl font-extrabold text-gray-900">₹{totalRevenue.toLocaleString('en-IN')}</div>
          </div>
          <div>
            <span className="text-[11px] font-bold text-gray-500 uppercase">Total Freight & Expenses</span>
            <div className="text-xl font-extrabold text-red-700">-₹{totalDeductions.toLocaleString('en-IN')} ({deductionRatio}%)</div>
          </div>
          <div>
            <span className="text-[11px] font-bold text-harvest-800 uppercase">Net Realized Proceeds</span>
            <div className="text-2xl font-black text-harvest-900">₹{netProceeds.toLocaleString('en-IN')}</div>
          </div>
          <div>
            <span className="text-[11px] font-bold text-harvest-800 uppercase">Effective Net Per {unitType}</span>
            <div className="text-2xl font-black text-harvest-900">₹{netProceedsPerUnit} /{unitType}</div>
          </div>
        </div>

        <div className="mt-4 pt-3 border-t border-harvest-200/80 flex items-center text-xs text-harvest-900 font-medium space-x-1.5">
          <Info className="w-4 h-4 flex-shrink-0 text-harvest-700" />
          <span>Note: Displayed estimates do not guarantee final spot auction clearing, quality grading, or weighbridge variance.</span>
        </div>
      </div>
    </div>
  );
}
