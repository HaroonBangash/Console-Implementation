import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";

import { UIProvider } from "@/components/providers/ui-provider";
import { ToastProviderWithState } from "@/components/ui/use-toast";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "SkillForge UI",
  description: "Accessible SkillForge experience prototype"
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en" suppressHydrationWarning>
      <body className={inter.className}>
        <ToastProviderWithState>
          <UIProvider>{children}</UIProvider>
        </ToastProviderWithState>
      </body>
    </html>
  );
}
