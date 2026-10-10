import React from 'react';
import { ShieldCheck, Database, Users, HelpCircle, PhoneCall, Mail } from 'lucide-react';
import { translations } from '../i18n/translations';

export default function AboutUs({ currentLang }) {
  const t = translations[currentLang] || translations.en;

  return (
    <section id="about-section" className="py-16 bg-white border-b border-gray-200">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        
        {/* Header */}
        <div className="max-w-3xl mb-12">
          <span className="text-xs font-black uppercase tracking-wider text-harvest-700 bg-harvest-100 px-3 py-1 rounded-full inline-block mb-2">
            Empowering Bharat
          </span>
          <h2 className="text-3xl sm:text-4xl font-black text-gray-900 tracking-tight">
            {t.aboutHeading}
          </h2>
          <p className="mt-3 text-base sm:text-lg text-gray-600 font-medium leading-relaxed">
            {t.aboutDesc}
          </p>
        </div>

        {/* 3 Value Pillars */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mb-12">
          <div className="bg-gray-50 rounded-2xl p-6 border border-gray-200/80">
            <div className="w-12 h-12 rounded-xl bg-harvest-100 text-harvest-800 flex items-center justify-center mb-4">
              <ShieldCheck className="w-6 h-6" />
            </div>
            <h3 className="font-extrabold text-lg text-gray-900 mb-2">Fair Value for Producers</h3>
            <p className="text-sm text-gray-600 leading-relaxed font-medium">
              We eliminate middlemen price opacity by giving farmers direct, accessible spot rates right before harvest negotiations.
            </p>
          </div>

          <div className="bg-gray-50 rounded-2xl p-6 border border-gray-200/80">
            <div className="w-12 h-12 rounded-xl bg-amber-100 text-amber-800 flex items-center justify-center mb-4">
              <Database className="w-6 h-6" />
            </div>
            <h3 className="font-extrabold text-lg text-gray-900 mb-2">Open API Ready</h3>
            <p className="text-sm text-gray-600 leading-relaxed font-medium">
              Architected with modular backend connectors designed to seamlessly plug into Agmarknet, eNAM, and state agriculture boards.
            </p>
          </div>

          <div className="bg-gray-50 rounded-2xl p-6 border border-gray-200/80">
            <div className="w-12 h-12 rounded-xl bg-blue-100 text-blue-800 flex items-center justify-center mb-4">
              <Users className="w-6 h-6" />
            </div>
            <h3 className="font-extrabold text-lg text-gray-900 mb-2">Farmer-First Accessibility</h3>
            <p className="text-sm text-gray-600 leading-relaxed font-medium">
              Multi-lingual support across English, Hindi, and Telugu, with voice-enabled search and high-contrast daylight-optimized views.
            </p>
          </div>
        </div>

        {/* Help & Contact Bar */}
        <div className="bg-gradient-to-r from-harvest-900 to-harvest-800 text-white rounded-2xl p-8 flex flex-col md:flex-row items-center justify-between gap-6 shadow-xl">
          <div>
            <h4 className="text-xl font-black">Need assistance or have market feedback?</h4>
            <p className="text-sm text-harvest-200 mt-1 font-medium">
              Our rural agricultural support desk helps farmers understand price movements.
            </p>
          </div>

          <div className="flex flex-wrap items-center gap-4">
            <a 
              href="tel:18001801551" 
              className="inline-flex items-center space-x-2 px-5 py-3 rounded-xl bg-white text-harvest-900 font-extrabold text-sm shadow hover:bg-harvest-50 transition-colors"
            >
              <PhoneCall className="w-4 h-4 text-harvest-700" />
              <span>Toll Free: 1800-180-1551</span>
            </a>
            <a 
              href="mailto:support@kisanprice.gov.in" 
              className="inline-flex items-center space-x-2 px-5 py-3 rounded-xl bg-harvest-700 hover:bg-harvest-600 text-white font-bold text-sm border border-harvest-600 transition-colors"
            >
              <Mail className="w-4 h-4" />
              <span>support@kisanprice.in</span>
            </a>
          </div>
        </div>

      </div>
    </section>
  );
}
