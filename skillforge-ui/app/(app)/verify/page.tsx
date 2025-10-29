"use client";

import * as React from "react";
import { ShieldCheck, ShieldX } from "lucide-react";

import { PageHeader } from "@/components/page-header";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";

export default function VerifyPage() {
  const [query, setQuery] = React.useState("");
  const [result, setResult] = React.useState<{ valid: boolean; hash: string } | null>(null);

  const handleVerify = (event: React.FormEvent) => {
    event.preventDefault();
    const isValid = query.toLowerCase().includes("abc123");
    setResult({ valid: isValid, hash: query });
  };

  return (
    <div className="space-y-6">
      <PageHeader
        title="Public credential verification"
        description="Employers can confirm a learner's credential using the verify URL or hash."
      />
      <Card>
        <CardHeader>
          <CardTitle>Verify credential</CardTitle>
          <CardDescription>Paste the verify link or on-chain hash to validate.</CardDescription>
        </CardHeader>
        <CardContent className="space-y-4">
          <form className="flex flex-col gap-3 md:flex-row" onSubmit={handleVerify}>
            <Input
              value={query}
              onChange={(event) => setQuery(event.target.value)}
              placeholder="https://skillforge.example/verify?hash=abc123"
              aria-label="Verify URL or hash"
            />
            <Button type="submit" className="shrink-0">
              Verify
            </Button>
          </form>
          {result ? (
            <div className="rounded-2xl border border-border p-4">
              <div className="flex items-center gap-3">
                {result.valid ? (
                  <ShieldCheck className="h-6 w-6 text-emerald-600" aria-hidden="true" />
                ) : (
                  <ShieldX className="h-6 w-6 text-red-500" aria-hidden="true" />
                )}
                <div>
                  <p className="text-sm font-semibold">{result.valid ? "Credential valid" : "Credential not found"}</p>
                  <p className="text-xs text-muted-foreground break-all">Hash: {result.hash || "—"}</p>
                </div>
              </div>
              <div className="mt-4 grid gap-3 md:grid-cols-2">
                <div className="space-y-1 text-sm">
                  <p className="font-medium">Candidate</p>
                  <p>Amina Hassan</p>
                </div>
                <div className="space-y-1 text-sm">
                  <p className="font-medium">Programme</p>
                  <p>AI-enabled Customer Success</p>
                </div>
                <div className="space-y-1 text-sm">
                  <p className="font-medium">Issuer</p>
                  <p>SkillForge Institute</p>
                </div>
                <div className="space-y-1 text-sm">
                  <p className="font-medium">On-chain status</p>
                  <Badge variant={result.valid ? "success" : "destructive"}>
                    {result.valid ? "VALID" : "INVALID"}
                  </Badge>
                </div>
              </div>
            </div>
          ) : null}
        </CardContent>
      </Card>
    </div>
  );
}
