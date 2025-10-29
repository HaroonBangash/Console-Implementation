"use client";

import * as React from "react";
import Link from "next/link";
import { ClipboardCheck } from "lucide-react";

import { PageHeader } from "@/components/page-header";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { assessments } from "@/mock-api/assessments";

const statusVariant: Record<string, "success" | "warning" | "outline"> = {
  released: "success",
  upcoming: "warning",
  awaiting: "warning",
  submitted: "outline"
};

export default function AssessmentsPage() {
  return (
    <div className="space-y-6">
      <PageHeader title="Assessments" description="Keep track of quizzes, labs, and peer projects." />
      <div className="grid gap-6 md:grid-cols-2">
        {assessments.map((assessment) => (
          <Card key={assessment.id}>
            <CardHeader>
              <CardTitle className="flex items-center gap-2 text-xl">
                <ClipboardCheck className="h-5 w-5" aria-hidden="true" /> {assessment.title}
              </CardTitle>
              <CardDescription>{assessment.relatedProgram}</CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="flex items-center gap-2 text-sm text-muted-foreground">
                <Badge variant={assessment.type === "quiz" ? "success" : "outline"}>
                  {assessment.type === "quiz" ? "Quiz" : "Project"}
                </Badge>
                <Badge variant={statusVariant[assessment.status] ?? "outline"}>{assessment.status}</Badge>
                <span>Due {new Date(assessment.dueDate).toLocaleDateString()}</span>
              </div>
              {assessment.score ? (
                <p className="text-sm text-muted-foreground">Score: {assessment.score}/{assessment.maxScore}</p>
              ) : null}
              <Button asChild>
                <Link href={`/assessment/${assessment.id}`}>Open assessment</Link>
              </Button>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
}
