"use client";

import * as React from "react";
import { Filter } from "lucide-react";

import { Button } from "@/components/ui/button";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";

export type FilterOption = {
  label: string;
  value: string;
};

type FilterBarProps = {
  industry: FilterOption[];
  duration: FilterOption[];
  outcome: FilterOption[];
  onChange: (filters: { industry: string; duration: string; outcome: string }) => void;
};

export function FilterBar({ industry, duration, outcome, onChange }: FilterBarProps) {
  const [filters, setFilters] = React.useState({ industry: "", duration: "", outcome: "" });

  React.useEffect(() => {
    onChange(filters);
  }, [filters, onChange]);

  return (
    <div className="flex flex-col gap-3 rounded-2xl border border-border bg-background p-4 sm:flex-row sm:items-center sm:justify-between">
      <div className="flex items-center gap-2 text-sm font-medium text-muted-foreground">
        <Filter className="h-4 w-4" aria-hidden="true" />
        <span>Filter programmes</span>
      </div>
      <div className="grid w-full gap-3 sm:grid-cols-3">
        <Select value={filters.industry} onValueChange={(value) => setFilters((prev) => ({ ...prev, industry: value }))}>
          <SelectTrigger aria-label="Filter by industry">
            <SelectValue placeholder="Industry" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="">All industries</SelectItem>
            {industry.map((option) => (
              <SelectItem key={option.value} value={option.value}>
                {option.label}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
        <Select value={filters.duration} onValueChange={(value) => setFilters((prev) => ({ ...prev, duration: value }))}>
          <SelectTrigger aria-label="Filter by duration">
            <SelectValue placeholder="Duration" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="">Any duration</SelectItem>
            {duration.map((option) => (
              <SelectItem key={option.value} value={option.value}>
                {option.label}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
        <Select value={filters.outcome} onValueChange={(value) => setFilters((prev) => ({ ...prev, outcome: value }))}>
          <SelectTrigger aria-label="Filter by outcome">
            <SelectValue placeholder="Outcome" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="">Any outcome</SelectItem>
            {outcome.map((option) => (
              <SelectItem key={option.value} value={option.value}>
                {option.label}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
      </div>
      <Button
        variant="ghost"
        className="justify-self-end text-xs"
        onClick={() => setFilters({ industry: "", duration: "", outcome: "" })}
      >
        Reset
      </Button>
    </div>
  );
}
