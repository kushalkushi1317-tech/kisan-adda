import React, { useState } from 'react';
import { X, User, Phone, CheckCircle2, Shield, Bell } from 'lucide-react';
import { translations } from '../i18n/translations';

export default function FarmerLoginModal({ isOpen, onClose, currentLang }) {
  const [phone, setPhone] = useState('');
  const [otpSent, setOtpSent] = useState(false);
  const [otp, setOtp] = useState('');
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [farmerName, setFarmerName] = useState('Ramesh Patel');

  if (!isOpen) return null;

  const handleSendOtp = (e) => {
    e.preventDefault();
    if (phone.length >= 10) {
      setOtpSent(true);
    }
  };

  const handleVerify = (e) => {
    e.preventDefault();
    setIsLoggedIn(true);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-gray-900/60 backdrop-blur-sm animate-in fade-in">
      <div className="bg-white rounded-3xl max-w-md w-full p-6 sm:p-8 shadow-2xl border border-gray-100 relative">
        <button
          onClick={onClose}
          className="absolute top-5 right-5 p-2 text-gray-400 hover:text-gray-600 rounded-full hover:bg-gray-100"
        >
          <X className="w-5 h-5" />
        </button>

        {!isLoggedIn ? (
          <div>
            <div className="flex items-center space-x-3 mb-6">
              <div className="w-12 h-12 rounded-2xl bg-harvest-100 text-harvest-800 flex items-center justify-center">
                <User className="w-6 h-6" />
              </div>
              <div>
                <h3 className="text-xl font-black text-gray-900">Farmer Login / Profile</h3>
                <p className="text-xs text-gray-500 font-medium">Access personalized mandi alerts & saved crops</p>
              </div>
            </div>

            {!otpSent ? (
              <form onSubmit={handleSendOtp} className="space-y-4">
                <div>
                  <label className="block text-xs font-bold text-gray-700 uppercase tracking-wider mb-1.5">
                    Mobile Number (10 Digits)
                  </label>
                  <div className="relative">
                    <span className="absolute inset-y-0 left-0 pl-3 flex items-center text-sm font-bold text-gray-500">
                      +91
                    </span>
                    <input
                      type="tel"
                      required
                      value={phone}
                      onChange={(e) => setPhone(e.target.value.replace(/\D/g, '').slice(0, 10))}
                      placeholder="98765 43210"
                      className="block w-full pl-12 pr-4 py-3 bg-gray-50 border border-gray-300 rounded-xl text-base font-bold text-gray-900 focus:outline-none focus:ring-2 focus:ring-harvest-600 focus:bg-white"
                    />
                  </div>
                </div>

                <button
                  type="submit"
                  disabled={phone.length < 10}
                  className="w-full py-3.5 rounded-xl bg-harvest-700 hover:bg-harvest-800 disabled:opacity-50 text-white font-extrabold text-sm shadow-md transition-all shadow-harvest-700/20"
                >
                  Get One-Time Password (OTP)
                </button>
              </form>
            ) : (
              <form onSubmit={handleVerify} className="space-y-4">
                <div className="p-3 bg-emerald-50 border border-emerald-200 rounded-xl text-xs font-semibold text-emerald-800">
                  OTP sent to +91 {phone}. (Use demo OTP: 1234)
                </div>

                <div>
                  <label className="block text-xs font-bold text-gray-700 uppercase tracking-wider mb-1.5">
                    Enter 4-Digit OTP
                  </label>
                  <input
                    type="text"
                    required
                    value={otp}
                    onChange={(e) => setOtp(e.target.value.slice(0, 4))}
                    placeholder="1234"
                    className="block w-full text-center py-3 bg-gray-50 border border-gray-300 rounded-xl text-xl font-black text-gray-900 tracking-widest focus:outline-none focus:ring-2 focus:ring-harvest-600 focus:bg-white"
                  />
                </div>

                <button
                  type="submit"
                  className="w-full py-3.5 rounded-xl bg-harvest-700 hover:bg-harvest-800 text-white font-extrabold text-sm shadow-md transition-all shadow-harvest-700/20"
                >
                  Verify & Open Dashboard
                </button>
              </form>
            )}
          </div>
        ) : (
          <div className="space-y-5">
            <div className="flex items-center space-x-3 pb-4 border-b border-gray-200">
              <div className="w-14 h-14 rounded-2xl bg-harvest-600 text-white flex items-center justify-center font-black text-xl">
                RP
              </div>
              <div>
                <h3 className="text-lg font-black text-gray-900">{farmerName}</h3>
                <span className="text-xs text-harvest-700 font-bold bg-harvest-50 px-2 py-0.5 rounded-full border border-harvest-200">
                  Verified Farmer ID #KP-8492
                </span>
              </div>
            </div>

            <div className="space-y-3">
              <h4 className="text-xs font-bold text-gray-500 uppercase tracking-wider">Your Monitored Crops</h4>
              <div className="p-3 bg-gray-50 rounded-xl flex items-center justify-between text-sm">
                <span className="font-bold text-gray-800">🍅 Tomato (Kolar Mandi)</span>
                <span className="font-extrabold text-harvest-700">₹35/kg</span>
              </div>
              <div className="p-3 bg-gray-50 rounded-xl flex items-center justify-between text-sm">
                <span className="font-bold text-gray-800">🧅 Onion (Lasalgaon Mandi)</span>
                <span className="font-extrabold text-harvest-700">₹31/kg</span>
              </div>
            </div>

            <button
              onClick={() => {
                setIsLoggedIn(false);
                setOtpSent(false);
                setPhone('');
                onClose();
              }}
              className="w-full py-2.5 rounded-xl border border-gray-200 hover:bg-gray-100 text-gray-700 text-xs font-bold"
            >
              Log Out
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
