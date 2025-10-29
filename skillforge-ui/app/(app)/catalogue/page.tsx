"use client";

import * as React from "react";
import Link from "next/link";
import { CalendarDays, Clock } from "lucide-react";

import { FilterBar } from "@/components/filter-bar";
import { PageHeader } from "@/components/page-header";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { programs } from "@/mock-api/programs";

const industryFilters = [
  { value: "IT", label: "IT" },
  { value: "Business", label: "Business" },
  { value: "Hospitality", label: "Hospitality" },
  { value: "Healthcare", label: "Healthcare" }
];

const durationFilters = [
  { value: "5", label: "5 weeks" },
  { value: "6", label: "6 weeks" },
  { value: "7", label: "7 weeks" },
  { value: "8", label: "8 weeks" }
];

const outcomeFilters = [
  { value: "Accreditation", label: "Accreditation" },
  { value: "Certification", label: "Certification" }
];

export default function CataloguePage() {
  const [filters, setFilters] = React.useState({ industry: "", duration: "", outcome: "" });

  const filteredPrograms = programs.filter((program) => {
    const matchesIndustry = filters.industry ? program.industry === filters.industry : true;
    const matchesDuration = filters.duration ? program.durationWeeks === Number(filters.duration) : true;
    const matchesOutcome = filters.outcome ? program.outcome === filters.outcome : true;
    return matchesIndustry && matchesDuration && matchesOutcome;
  });

  return (
    <div className="space-y-6">
      <PageHeader
        title="Programme catalogue"
        description="Explore SkillForge pathways curated with industry partners."
      />
      <FilterBar
        industry={industryFilters}
        duration={durationFilters}
        outcome={outcomeFilters}
        onChange={setFilters}
      />
      <div className="grid gap-6 md:grid-cols-2 xl:grid-cols-3">
        {filteredPrograms.map((program) => (
          <Card key={program.id} className="flex flex-col">
            <CardHeader>
              <CardTitle className="text-xl">{program.title}</CardTitle>
              <CardDescription>{program.summary}</CardDescription>
            </CardHeader>
            <CardContent className="flex flex-1 flex-col justify-between gap-4 text-sm text-muted-foreground">
              <div className="space-y-2">
                <div className="flex items-center gap-2">
                  <Badge variant="outline">{program.industry}</Badge>
                  <Badge variant={program.accreditation ? "success" : "outline"}>
                    {program.accreditation ? "Accredited" : "Certification"}
                  </Badge>
                </div>
                <div className="flex items-center gap-2">
                  <Clock className="h-4 w-4" aria-hidden="true" /> {program.durationWeeks} weeks
                </div>
                <div className="flex items-center gap-2">
                  <CalendarDays className="h-4 w-4" aria-hidden="true" /> Starts {new Date(program.startDate).toLocaleDateString()}
                </div>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-xs uppercase tracking-wide text-muted-foreground">Outcome: {program.outcome}</span>
                <Button asChild>
                  <Link href={`/programme/${program.id}`}>View</Link>
                </Button>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
}
