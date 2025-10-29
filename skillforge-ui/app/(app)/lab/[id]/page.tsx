"use client";

import * as React from "react";
import Link from "next/link";
import { useParams } from "next/navigation";
import { CheckCircle2, ClipboardList, Timer } from "lucide-react";

import { PageHeader } from "@/components/page-header";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Textarea } from "@/components/ui/textarea";
import { useToast } from "@/components/ui/use-toast";
import { labs } from "@/mock-api/labs";

export default function LabSessionPage() {
  const params = useParams<{ id: string }>();
  const { addToast } = useToast();
  const lab = labs.find((item) => item.id === params?.id);
  const [remaining, setRemaining] = React.useState(45 * 60);
  const [notes, setNotes] = React.useState("");
  const [evidence, setEvidence] = React.useState(lab?.evidence ?? []);

  React.useEffect(() => {
    const interval = window.setInterval(() => {
      setRemaining((prev) => (prev > 0 ? prev - 1 : 0));
    }, 1000);
    return () => window.clearInterval(interval);
  }, []);

  if (!lab) {
    return (
      <div className="space-y-6">
        <PageHeader title="Lab not found" description="Return to the dashboard to launch a session." />
        <Button asChild>
          <Link href="/dashboard">Back to dashboard</Link>
        </Button>
      </div>
    );
  }

  const formattedTime = new Date(remaining * 1000).toISOString().substring(14, 19);

  const handleAddEvidence = () => {
    const id = `evidence-${Date.now()}`;
    const item = { id, label: `Uploaded log ${evidence.length + 1}` };
    setEvidence((prev) => [...prev, item]);
    addToast({ title: "Evidence added", description: `${item.label} attached.` });
  };

  const handleSubmit = () => {
    addToast({ title: "Lab evidence submitted", description: "Assessment team notified." });
  };

  return (
    <div className="space-y-6">
      <PageHeader title={lab.title} description="Live virtual environment with auto-save for notes and outputs." />
      <Card>
        <CardHeader className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
          <div className="flex items-center gap-3 text-sm text-muted-foreground">
            <Timer className="h-4 w-4" aria-hidden="true" /> Remaining time <span className="font-semibold text-foreground">{formattedTime}</span>
          </div>
          <div className="flex items-center gap-3 text-sm text-muted-foreground">
            <Badge variant="outline">Attempts left: {lab.maxAttempts - lab.attempts}</Badge>
            <Button variant="outline" onClick={() => addToast({ title: "Session ended", description: "Lab session saved." })}>
              End session
            </Button>
          </div>
        </CardHeader>
        <CardContent className="grid gap-6 md:grid-cols-[1.2fr_1fr]">
          <section aria-label="Lab steps" className="space-y-4">
            <Card className="h-full">
              <CardHeader>
                <CardTitle className="flex items-center gap-2 text-lg">
                  <ClipboardList className="h-5 w-5" /> Step checklist
                </CardTitle>
                <CardDescription>Mark off tasks as you capture evidence.</CardDescription>
              </CardHeader>
              <CardContent>
                <ul className="space-y-3 text-sm">
                  {lab.checklist.map((item) => (
                    <li key={item} className="flex items-start gap-3">
                      <CheckCircle2 className="mt-0.5 h-4 w-4 text-primary" aria-hidden="true" />
                      <span>{item}</span>
                    </li>
                  ))}
                </ul>
              </CardContent>
            </Card>
            <div className="space-y-3">
              <label htmlFor="lab-notes" className="text-sm font-medium">
                Session notes
              </label>
              <Textarea
                id="lab-notes"
                placeholder="Record findings, commands, or anomalies..."
                value={notes}
                onChange={(event) => setNotes(event.target.value)}
              />
            </div>
          </section>
          <section aria-label="Evidence" className="space-y-4">
            <Card className="h-full">
              <CardHeader>
                <CardTitle className="text-lg">Evidence panel</CardTitle>
                <CardDescription>Attach screenshots, logs, or exports for reviewers.</CardDescription>
              </CardHeader>
              <CardContent className="space-y-3">
                <div className="space-y-2">
                  {evidence.length ? (
                    <ul className="space-y-2 text-sm">
                      {evidence.map((item) => (
                        <li key={item.id} className="flex items-center justify-between rounded-xl border border-border px-3 py-2">
                          <span>{item.label}</span>
                          <Badge variant="success">Ready</Badge>
                        </li>
                      ))}
                    </ul>
                  ) : (
                    <p className="text-sm text-muted-foreground">No evidence yet. Add outputs as you work.</p>
                  )}
                </div>
                <Button variant="secondary" onClick={handleAddEvidence}>
                  Add evidence
                </Button>
              </CardContent>
            </Card>
            <Button onClick={handleSubmit} className="w-full">
              Submit evidence to assessment
            </Button>
          </section>
        </CardContent>
      </Card>
    </div>
  );
}
