"use client";

import * as React from "react";

type UIContextValue = {
  highContrast: boolean;
  toggleHighContrast: () => void;
  language: "EN" | "ES" | "AR";
  setLanguage: (language: "EN" | "ES" | "AR") => void;
};

const UIContext = React.createContext<UIContextValue | undefined>(undefined);

export function UIProvider({ children }: { children: React.ReactNode }) {
  const [highContrast, setHighContrast] = React.useState(false);
  const [language, setLanguage] = React.useState<"EN" | "ES" | "AR">("EN");

  React.useEffect(() => {
    if (typeof document !== "undefined") {
      document.documentElement.dataset.contrast = highContrast ? "high" : "normal";
    }
  }, [highContrast]);

  const value = React.useMemo(
    () => ({
      highContrast,
      toggleHighContrast: () => setHighContrast((prev) => !prev),
      language,
      setLanguage
    }),
    [highContrast, language]
  );

  return <UIContext.Provider value={value}>{children}</UIContext.Provider>;
}

export function useUI() {
  const context = React.useContext(UIContext);
  if (!context) {
    throw new Error("useUI must be used within UIProvider");
  }
  return context;
}
