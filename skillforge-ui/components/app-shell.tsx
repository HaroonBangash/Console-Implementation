"use client";

import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import * as React from "react";
import { Bell, Globe, LogOut, Menu, Search, User } from "lucide-react";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Switch } from "@/components/ui/switch";
import { useToast } from "@/components/ui/use-toast";
import { useUI } from "@/components/providers/ui-provider";

const navItems = [
  { href: "/dashboard", label: "Dashboard" },
  { href: "/catalogue", label: "Catalogue" },
  { href: "/my-courses", label: "My Courses" },
  { href: "/labs", label: "Labs" },
  { href: "/assessments", label: "Assessments" },
  { href: "/credentials", label: "Credentials" },
  { href: "/jobs", label: "Jobs" },
  { href: "/verify", label: "Verify" },
  { href: "/trainer", label: "Trainer" },
  { href: "/admin", label: "Admin" },
  { href: "/analytics", label: "Analytics" },
  { href: "/help", label: "Help" }
];

export function AppShell({ children }: { children: React.ReactNode }) {
  const pathname = usePathname();
  const router = useRouter();
  const { language, setLanguage, highContrast, toggleHighContrast } = useUI();
  const { addToast } = useToast();
  const [sidebarOpen, setSidebarOpen] = React.useState(false);

  React.useEffect(() => {
    setSidebarOpen(false);
  }, [pathname]);

  return (
    <div className="flex min-h-screen flex-col bg-muted/10">
      <a className="skip-link" href="#main">
        Skip to content
      </a>
      <header className="sticky top-0 z-30 border-b border-border bg-background/95 backdrop-blur">
        <div className="mx-auto flex w-full max-w-7xl items-center justify-between gap-4 px-4 py-3">
          <div className="flex items-center gap-3">
            <Button
              variant="ghost"
              size="icon"
              className="md:hidden"
              aria-label="Toggle navigation"
              onClick={() => setSidebarOpen((prev) => !prev)}
            >
              <Menu className="h-5 w-5" />
            </Button>
            <Link href="/dashboard" className="text-lg font-semibold">
              SkillForge
            </Link>
          </div>
          <div className="hidden flex-1 items-center gap-3 md:flex">
            <div className="relative flex-1">
              <Search className="pointer-events-none absolute left-4 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
              <Input className="pl-12" placeholder="Search programmes, labs, credentials" aria-label="Search" />
            </div>
            <Button
              variant="ghost"
              size="icon"
              aria-label="Notifications"
              onClick={() => addToast({ title: "Notifications", description: "You are up to date." })}
            >
              <Bell className="h-5 w-5" />
            </Button>
            <div className="hidden items-center gap-2 lg:flex">
              <span className="text-xs font-medium uppercase text-muted-foreground">High Contrast</span>
              <Switch
                checked={highContrast}
                onCheckedChange={toggleHighContrast}
                aria-label="Toggle high contrast mode"
              />
            </div>
            <Select value={language} onValueChange={(value) => setLanguage(value as "EN" | "ES" | "AR")}>
              <SelectTrigger className="w-[120px]" aria-label="Select language">
                <SelectValue placeholder="Language" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="EN">English</SelectItem>
                <SelectItem value="ES">Español</SelectItem>
                <SelectItem value="AR">العربية</SelectItem>
              </SelectContent>
            </Select>
            <Button
              variant="ghost"
              className="gap-2"
              onClick={() => router.push("/profile")}
              aria-label="User menu"
            >
              <User className="h-5 w-5" />
              Profile
            </Button>
          </div>
          <div className="flex items-center gap-2 md:hidden">
            <Switch
              checked={highContrast}
              onCheckedChange={toggleHighContrast}
              aria-label="Toggle high contrast mode"
            />
            <Button
              variant="ghost"
              size="icon"
              aria-label="Open notifications"
              onClick={() => addToast({ title: "Notifications", description: "No new alerts." })}
            >
              <Bell className="h-5 w-5" />
            </Button>
          </div>
        </div>
      </header>
      <div className="mx-auto flex w-full max-w-7xl flex-1 gap-6 px-4 py-6">
        <aside
          className={`${sidebarOpen ? "block" : "hidden"} h-full w-60 shrink-0 rounded-2xl border border-border bg-background p-4 md:block`}
          aria-label="Primary navigation"
        >
          <nav className="flex flex-col gap-1">
            {navItems.map((item) => {
              const active = pathname === item.href;
              return (
                <Link
                  key={item.href}
                  href={item.href}
                  className={`flex items-center justify-between rounded-xl px-4 py-2 text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 ${
                    active ? "bg-primary/10 text-primary" : "hover:bg-muted"
                  }`}
                >
                  {item.label}
                </Link>
              );
            })}
          </nav>
        </aside>
        <main id="main" className="flex-1 space-y-6 pb-16">
          <div className="flex flex-col gap-2 rounded-2xl border border-border bg-background p-4 text-sm text-muted-foreground md:hidden">
            <div className="flex items-center gap-2">
              <Globe className="h-4 w-4" />
              <span>Language</span>
              <Select value={language} onValueChange={(value) => setLanguage(value as "EN" | "ES" | "AR")}>
                <SelectTrigger className="w-[120px]">
                  <SelectValue placeholder="Language" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="EN">English</SelectItem>
                  <SelectItem value="ES">Español</SelectItem>
                  <SelectItem value="AR">العربية</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="flex items-center gap-2">
              <span className="text-xs font-medium uppercase text-muted-foreground">High Contrast</span>
              <Switch
                checked={highContrast}
                onCheckedChange={toggleHighContrast}
                aria-label="Toggle high contrast mode"
              />
            </div>
          </div>
          {children}
        </main>
      </div>
      <footer className="border-t border-border bg-background px-4 py-6 text-sm text-muted-foreground">
        <div className="mx-auto flex w-full max-w-7xl flex-wrap items-center justify-between gap-3">
          <span>© {new Date().getFullYear()} SkillForge.</span>
          <div className="flex items-center gap-3">
            <Button variant="link" className="px-0" onClick={() => addToast({ title: "Offline", description: "Downloads synced." })}>
              Offline downloads
            </Button>
            <Button variant="link" className="px-0" onClick={() => addToast({ title: "Accessibility", description: "Review the accessibility guide." })}>
              Accessibility
            </Button>
            <Button
              variant="ghost"
              size="icon"
              aria-label="Sign out"
              onClick={() => router.push("/login")}
            >
              <LogOut className="h-4 w-4" />
            </Button>
          </div>
        </div>
      </footer>
    </div>
  );
}
