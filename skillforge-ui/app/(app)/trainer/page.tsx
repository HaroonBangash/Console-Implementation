"use client";

import * as React from "react";
import Link from "next/link";
import { Activity, ClipboardList, GraduationCap } from "lucide-react";

import { PageHeader } from "@/components/page-header";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";

const markingQueue = [
  { id: "submission-1", learner: "Sofia Patel", assessment: "Lab Runbook Evidence", submitted: "2h ago" },
  { id: "submission-2", learner: "Diego Alvarez", assessment: "Incident Simulation Project", submitted: "5h ago" }
];

export default function TrainerDashboardPage() {
  return (
    <div className="space-y-6">
      <PageHeader
        title="Trainer dashboard"
        description="Monitor cohort progress, manage assessments, and launch new programmes."
        actions={
          <Button asChild>
            <Link href="/analytics">Open analytics</Link>
          </Button>
        }
      />
      <div className="grid gap-6 md:grid-cols-3">
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2 text-xl">
              <GraduationCap className="h-5 w-5" aria-hidden="true" /> Cohort progress
            </CardTitle>
            <CardDescription>Week 4 of 8</CardDescription>
          </CardHeader>
          <CardContent className="text-sm text-muted-foreground">
            <p>Average completion: 68%</p>
            <p className="mt-2 rounded-2xl border border-dashed border-border p-3">Sparkline: ▂▄▆▇█</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2 text-xl">
              <ClipboardList className="h-5 w-5" aria-hidden="true" /> Marking queue
            </CardTitle>
            <CardDescription>{markingQueue.length} submissions awaiting review</CardDescription>
          </CardHeader>
          <CardContent className="space-y-2 text-sm text-muted-foreground">
            {markingQueue.map((item) => (
              <div key={item.id} className="flex items-center justify-between rounded-xl border border-border px-3 py-2">
                <div>
                  <p className="font-medium text-foreground">{item.learner}</p>
                  <p>{item.assessment}</p>
                </div>
                <Badge variant="outline">{item.submitted}</Badge>
              </div>
            ))}
            <Button variant="link" className="px-0" asChild>
              <Link href="#">View queue</Link>
            </Button>
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2 text-xl">
              <Activity className="h-5 w-5" aria-hidden="true" /> Analytics
            </CardTitle>
            <CardDescription>Track learner outcomes</CardDescription>
          </CardHeader>
          <CardContent className="space-y-3 text-sm text-muted-foreground">
            <p>Engagement up 12% week-on-week.</p>
            <Button asChild>
              <Link href="/analytics">Go to analytics</Link>
            </Button>
          </CardContent>
        </Card>
      </div>
      <div className="grid gap-6 lg:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle>Create programme</CardTitle>
            <CardDescription>Draft new learning paths with competency frameworks.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-4 text-sm text-muted-foreground">
            <p>Start from templates or clone existing programmes.</p>
            <Button variant="secondary">Launch builder</Button>
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>Grade submissions</CardTitle>
            <CardDescription>Recent evidence and peer review summaries.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Learner</TableHead>
                  <TableHead>Assessment</TableHead>
                  <TableHead>Submitted</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {markingQueue.map((item) => (
                  <TableRow key={item.id}>
                    <TableCell>{item.learner}</TableCell>
                    <TableCell>{item.assessment}</TableCell>
                    <TableCell>{item.submitted}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
            <Button variant="outline">Open grading workspace</Button>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
