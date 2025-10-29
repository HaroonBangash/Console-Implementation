"use client";

import * as React from "react";
import { useParams } from "next/navigation";
import { Upload } from "lucide-react";

import { PageHeader } from "@/components/page-header";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { useToast } from "@/components/ui/use-toast";
import { assessments } from "@/mock-api/assessments";

const quizQuestions = [
  {
    id: "q1",
    prompt: "Which signal best confirms a saturation issue in telemetry pipelines?",
    options: ["Increased CPU utilisation", "Dropped span percentage", "Healthy log throughput"],
    answer: "Dropped span percentage"
  },
  {
    id: "q2",
    prompt: "Which playbook step comes before incident declaration?",
    options: ["Post-mortem", "Severity assessment", "Stakeholder broadcast"],
    answer: "Severity assessment"
  }
];

export default function AssessmentPage() {
  const params = useParams<{ id: string }>();
  const { addToast } = useToast();
  const assessment = assessments.find((item) => item.id === params?.id);
  const [selectedAnswers, setSelectedAnswers] = React.useState<Record<string, string>>({});
  const [quizResult, setQuizResult] = React.useState<{ score: number; total: number } | null>(null);
  const [projectStatus, setProjectStatus] = React.useState<"Awaiting Reviews" | "Aggregating" | "Released">(
    "Awaiting Reviews"
  );

  if (!assessment) {
    return (
      <div className="space-y-6">
        <PageHeader title="Assessment not found" description="Return to your dashboard to pick another." />
      </div>
    );
  }

  const handleQuizSubmit = () => {
    const total = quizQuestions.length;
    const score = quizQuestions.reduce((acc, question) => (selectedAnswers[question.id] === question.answer ? acc + 1 : acc), 0);
    setQuizResult({ score, total });
    addToast({ title: "Quiz auto-graded", description: `You scored ${score}/${total}.` });
  };

  const cycleStatus = () => {
    setProjectStatus((current) => {
      if (current === "Awaiting Reviews") {
        addToast({ title: "Peer reviews requested", description: "3 reviewers notified." });
        return "Aggregating";
      }
      if (current === "Aggregating") {
        addToast({ title: "Scores released", description: "Learners notified of outcomes." });
        return "Released";
      }
      addToast({ title: "Already released", description: "Scores are available to learners." });
      return "Released";
    });
  };

  return (
    <div className="space-y-6">
      <PageHeader title={assessment.title} description={`Assessment type: ${assessment.type === "quiz" ? "Quiz" : "Project"}.`} />
      {assessment.type === "quiz" ? (
        <Card>
          <CardHeader>
            <CardTitle className="text-xl">Quiz attempt</CardTitle>
            <CardDescription>Answer the questions below. Auto-grading runs immediately.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-6">
            {quizQuestions.map((question, index) => (
              <div key={question.id} className="space-y-3 rounded-2xl border border-border p-4">
                <div className="flex items-start justify-between gap-3">
                  <div>
                    <p className="text-sm font-semibold uppercase text-muted-foreground">Question {index + 1}</p>
                    <p className="text-base font-medium">{question.prompt}</p>
                  </div>
                </div>
                <RadioGroup
                  value={selectedAnswers[question.id] ?? ""}
                  onValueChange={(value) => setSelectedAnswers((prev) => ({ ...prev, [question.id]: value }))}
                >
                  {question.options.map((option) => (
                    <div key={option} className="flex items-center gap-3 rounded-xl border border-border/70 p-3">
                      <RadioGroupItem id={`${question.id}-${option}`} value={option} />
                      <Label htmlFor={`${question.id}-${option}`} className="text-sm font-medium">
                        {option}
                      </Label>
                    </div>
                  ))}
                </RadioGroup>
              </div>
            ))}
            <div className="flex items-center justify-between rounded-2xl border border-dashed border-border p-4 text-sm text-muted-foreground">
              <span>Auto-grading uses answer keys and provides instant feedback.</span>
              <Button onClick={handleQuizSubmit}>Submit quiz</Button>
            </div>
            {quizResult ? (
              <div className="rounded-2xl border border-success/40 bg-success/10 p-4 text-sm">
                <p className="font-semibold">Auto-graded</p>
                <p>Score: {quizResult.score} / {quizResult.total}</p>
              </div>
            ) : null}
          </CardContent>
        </Card>
      ) : (
        <Card>
          <CardHeader>
            <CardTitle className="text-xl">Project submission</CardTitle>
            <CardDescription>Upload artefacts and coordinate peer reviews.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-6">
            <div className="space-y-3 rounded-2xl border border-border p-4">
              <Label htmlFor="project-upload" className="flex items-center gap-2 text-sm font-medium">
                <Upload className="h-4 w-4" /> Upload deliverables
              </Label>
              <Input id="project-upload" type="file" aria-describedby="project-upload-help" />
              <p id="project-upload-help" className="text-xs text-muted-foreground">
                Accepted types: PDF, DOCX, or ZIP (max 150MB).
              </p>
              <Button variant="secondary" onClick={() => addToast({ title: "Upload queued", description: "Files syncing to reviewers." })}>
                Submit files
              </Button>
            </div>
            <div className="space-y-3 rounded-2xl border border-dashed border-border p-4">
              <h3 className="text-sm font-semibold uppercase text-muted-foreground">Assign peers</h3>
              <p className="text-sm text-muted-foreground">
                Peer reviewers: Priya Menon, Carlos Ibarra, and An Nguyen. They will review within 48 hours.
              </p>
              <Button variant="outline" onClick={() => addToast({ title: "Peers confirmed", description: "Reviewers notified." })}>
                Confirm reviewers
              </Button>
            </div>
            <div className="space-y-3">
              <h3 className="text-sm font-semibold uppercase text-muted-foreground">Status timeline</h3>
              <div className="flex flex-wrap items-center gap-3">
                <Badge variant={projectStatus === "Awaiting Reviews" ? "warning" : "outline"}>Awaiting Reviews</Badge>
                <Badge variant={projectStatus === "Aggregating" ? "warning" : "outline"}>Aggregating</Badge>
                <Badge variant={projectStatus === "Released" ? "success" : "outline"}>Released</Badge>
              </div>
              <Button onClick={cycleStatus} variant="secondary">
                {projectStatus === "Released" ? "Scores released" : "Advance status"}
              </Button>
            </div>
            <div className="rounded-2xl border border-border p-4 text-sm text-muted-foreground">
              <p className="font-medium">Assessment rubric</p>
              <p>Review alignment to scenario brief, incident communication quality, and automated detection coverage.</p>
            </div>
          </CardContent>
        </Card>
      )}
    </div>
  );
}
