"use client";

import * as React from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { ShieldCheck } from "lucide-react";

import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Switch } from "@/components/ui/switch";
import { useToast } from "@/components/ui/use-toast";
import { useUI } from "@/components/providers/ui-provider";

export default function LoginPage() {
  const router = useRouter();
  const { addToast } = useToast();
  const { highContrast, toggleHighContrast, language, setLanguage } = useUI();

  const [step, setStep] = React.useState<"login" | "mfa">("login");
  const [email, setEmail] = React.useState("");
  const [password, setPassword] = React.useState("");
  const [mfaCode, setMfaCode] = React.useState("");
  const [error, setError] = React.useState<string | null>(null);
  const [mfaError, setMfaError] = React.useState<string | null>(null);

  const handleNext = (event: React.FormEvent) => {
    event.preventDefault();
    if (!email || !password) {
      setError("Enter your email and password to continue.");
      return;
    }
    setError(null);
    setStep("mfa");
    addToast({ title: "MFA sent", description: "Check your authenticator app for a 6-digit code." });
  };

  const handleVerify = (event: React.FormEvent) => {
    event.preventDefault();
    if (mfaCode.length !== 6) {
      setMfaError("Enter the 6-digit code.");
      return;
    }
    setMfaError(null);
    addToast({ title: "Welcome back", description: "Redirecting to your dashboard." });
    router.push("/dashboard");
  };

  return (
    <div className="flex min-h-screen flex-col">
      <header className="border-b border-border bg-background/80">
        <div className="mx-auto flex w-full max-w-4xl items-center justify-between gap-4 px-6 py-4">
          <Link href="/login" className="text-lg font-semibold">
            SkillForge
          </Link>
          <div className="flex items-center gap-4 text-sm">
            <div className="flex items-center gap-2">
              <span className="hidden text-xs font-medium uppercase text-muted-foreground sm:inline">High Contrast</span>
              <Switch checked={highContrast} onCheckedChange={toggleHighContrast} aria-label="Toggle high contrast mode" />
            </div>
            <Select value={language} onValueChange={(value) => setLanguage(value as "EN" | "ES" | "AR")}>
              <SelectTrigger className="w-[120px]" aria-label="Choose language">
                <SelectValue placeholder="Language" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="EN">English</SelectItem>
                <SelectItem value="ES">Español</SelectItem>
                <SelectItem value="AR">العربية</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </div>
      </header>
      <main className="flex flex-1 items-center justify-center px-4 py-12">
        <div className="grid w-full max-w-4xl gap-10 lg:grid-cols-[1.2fr_1fr]">
          <div className="space-y-6">
            <h1 className="text-3xl font-semibold">Sign in to SkillForge</h1>
            <p className="text-sm text-muted-foreground">
              Securely access your personalised dashboard, labs, assessments, and credentials.
            </p>
            <Card className="shadow-lg">
              <CardHeader>
                <CardTitle>Login</CardTitle>
                <CardDescription>Use your institutional email address to sign in.</CardDescription>
              </CardHeader>
              <CardContent>
                <form className="space-y-4" onSubmit={handleNext} aria-label="Login form">
                  <div className="space-y-2">
                    <Label htmlFor="email">Email</Label>
                    <Input
                      id="email"
                      type="email"
                      autoComplete="email"
                      value={email}
                      onChange={(event) => setEmail(event.target.value)}
                      required
                    />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="password">Password</Label>
                    <Input
                      id="password"
                      type="password"
                      autoComplete="current-password"
                      value={password}
                      onChange={(event) => setPassword(event.target.value)}
                      required
                    />
                  </div>
                  {error ? <p className="text-sm text-destructive">{error}</p> : null}
                  <Button type="submit" className="w-full">
                    Next
                  </Button>
                </form>
              </CardContent>
            </Card>
          </div>
          <Card className="border-dashed">
            <CardHeader>
              <CardTitle className="flex items-center gap-2 text-xl">
                <ShieldCheck className="h-5 w-5" /> Multi-factor Authentication
              </CardTitle>
              <CardDescription>
                Protect your learning journey with secure, standards-aligned MFA. Codes refresh every 30 seconds.
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-3 text-sm text-muted-foreground">
              <p>Use your authenticator app or security key for the best experience.</p>
              <p>
                Need help? Visit the <Link className="font-medium text-primary" href="/help">support centre</Link>.
              </p>
            </CardContent>
          </Card>
        </div>
      </main>
      <footer className="border-t border-border bg-background px-6 py-4 text-sm text-muted-foreground">
        <div className="mx-auto flex w-full max-w-4xl flex-wrap items-center justify-between gap-3">
          <div className="flex items-center gap-2">
            <Select value={language} onValueChange={(value) => setLanguage(value as "EN" | "ES" | "AR")}>
              <SelectTrigger className="w-[140px]">
                <SelectValue placeholder="Language" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="EN">English</SelectItem>
                <SelectItem value="ES">Español</SelectItem>
                <SelectItem value="AR">العربية</SelectItem>
              </SelectContent>
            </Select>
          </div>
          <Link href="/accessibility" className="text-sm underline">
            Accessibility
          </Link>
        </div>
      </footer>

      {step === "mfa" ? (
        <div role="dialog" aria-modal="true" className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 px-4">
          <Card className="w-full max-w-md">
            <CardHeader>
              <CardTitle>Enter MFA code</CardTitle>
              <CardDescription>Approve sign-in by typing the six digits from your authenticator app.</CardDescription>
            </CardHeader>
            <CardContent>
              <form className="space-y-4" onSubmit={handleVerify}>
                <div className="space-y-2">
                  <Label htmlFor="mfa-code">6-digit code</Label>
                  <Input
                    id="mfa-code"
                    inputMode="numeric"
                    pattern="[0-9]*"
                    maxLength={6}
                    value={mfaCode}
                    onChange={(event) => setMfaCode(event.target.value.replace(/[^0-9]/g, ""))}
                    autoFocus
                  />
                </div>
                {mfaError ? <p className="text-sm text-destructive">{mfaError}</p> : null}
                <div className="flex items-center justify-between text-sm">
                  <button
                    type="button"
                    className="text-primary underline"
                    onClick={() => addToast({ title: "Code resent", description: "We sent a new MFA code." })}
                  >
                    Resend code
                  </button>
                  <Button type="submit">Verify</Button>
                </div>
              </form>
            </CardContent>
          </Card>
        </div>
      ) : null}
    </div>
  );
}
