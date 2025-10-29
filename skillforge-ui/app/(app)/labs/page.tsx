"use client";

import * as React from "react";
import Link from "next/link";
import { FlaskConical } from "lucide-react";

import { PageHeader } from "@/components/page-header";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { labs } from "@/mock-api/labs";

export default function LabsPage() {
  return (
    <div className="space-y-6">
      <PageHeader title="Virtual labs" description="Immersive environments to practice real-world scenarios." />
      <div className="grid gap-6 md:grid-cols-2">
        {labs.map((lab) => (
          <Card key={lab.id}>
            <CardHeader>
              <CardTitle className="flex items-center gap-2 text-xl">
                <FlaskConical className="h-5 w-5" aria-hidden="true" /> {lab.title}
              </CardTitle>
              <CardDescription>Attempts left: {lab.maxAttempts - lab.attempts}</CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="space-y-2 text-sm text-muted-foreground">
                <p>Checklist:</p>
                <ul className="list-inside list-disc space-y-1">
                  {lab.checklist.map((item) => (
                    <li key={item}>{item}</li>
                  ))}
                </ul>
              </div>
              <Button asChild>
                <Link href={`/lab/${lab.id}`}>Launch lab</Link>
              </Button>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
}
